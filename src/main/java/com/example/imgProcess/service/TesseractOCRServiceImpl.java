package com.example.imgProcess.service;

import com.example.imgProcess.util.CommonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@Log4j2
public class TesseractOCRServiceImpl implements OCRService{

    @Value("${THREAD_COUNT}")
    private String numThreads;

    @Autowired
    private CommonUtil commonUtil;

    @Override
    public String convertToText(List<MultipartFile> imageFiles) {
        int totalRecords = imageFiles.size();
        try{
            int noOfThreads = Integer.parseInt(numThreads);
            int batchSize = totalRecords / noOfThreads ;

            Thread[] threads = new Thread[noOfThreads];

            for (int i = 0; i < noOfThreads; i++) {
                final int start = i * batchSize;
                final int end = start + batchSize;

                threads[i] = new Thread(() -> {
                    for(int j = start; j<= end; j++){
                        commonUtil.convertImageToText(imageFiles.get(j));
                    }
                });
                threads[i].start();
            }

            // Wait for all threads to finish
            try {
                for (Thread thread : threads) {
                    thread.join();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return "Image parsed successfully.";
        } catch (Exception e) {
            log.info("Generic Exception Occurred :: {} ", e.getMessage());
        }
        return "Processing failed.";
    }
}
