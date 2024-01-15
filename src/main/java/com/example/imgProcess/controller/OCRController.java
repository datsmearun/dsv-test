package com.example.imgProcess.controller;

import com.example.imgProcess.service.TesseractOCRServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class OCRController {

    @Autowired
    TesseractOCRServiceImpl tesseractOCRService;

    @PostMapping("/convert")
    public String parseText(@ModelAttribute("files") List<MultipartFile> files) throws IOException {
        return tesseractOCRService.convertToText(files);
    }
}
