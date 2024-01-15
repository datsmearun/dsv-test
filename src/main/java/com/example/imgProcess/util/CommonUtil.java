package com.example.imgProcess.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.log4j.Log4j2;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

@Component
@Log4j2
public class CommonUtil {

    @Autowired
    private Tesseract tesseract;

    public void createPDFfile(String text, String filename) throws IOException, DocumentException {

        //Define Size of PDF file
        Document pdfDoc = new Document(PageSize.A4);
        PdfWriter.getInstance(pdfDoc, new FileOutputStream("src/main/resources/output/" +filename+ ".pdf"))
                .setPdfVersion(PdfWriter.VERSION_1_5);
        pdfDoc.open();

        //Setting the font
        Font myfont = new Font();
        myfont.setStyle(Font.NORMAL);
        myfont.setSize(11);
        pdfDoc.add(new Paragraph("\n"));

        //Add data to the pdf file
        BufferedReader br = new BufferedReader(new FileReader(filename));
        while ((text = br.readLine()) != null) {
            Paragraph para = new Paragraph(text + "\n", myfont);
            para.setAlignment(Element.ALIGN_JUSTIFIED);
            pdfDoc.add(para);
        }
        pdfDoc.close();
        br.close();

    }

    public void convertImageToText(MultipartFile file){
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            String parsedText = tesseract.doOCR(image);
            //Convert parsedText to PDF
            createPDFfile(parsedText, file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf(".")));
        } catch (IOException | TesseractException e) {
            log.info("Processing Exception Occurred :: {} ", e.getMessage());
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            log.info("Document Exception Occurred :: {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
