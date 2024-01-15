package com.example.imgProcess.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface OCRService {

    public String convertToText(List<MultipartFile> imageFiles) throws IOException;

}
