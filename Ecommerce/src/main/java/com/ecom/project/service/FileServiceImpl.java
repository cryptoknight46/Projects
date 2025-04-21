package com.ecom.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceImpl  implements FileService{
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //filenames of current/original file
        String originalFileName=file.getOriginalFilename();
        //Generate a unique a filename using u uid
        String randomId= UUID.randomUUID().toString();
        //ex: mat.jpg -- ()->4324 --> 4324.jpg
        String fileName=randomId.concat(
                originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath=path+ File.separator +fileName;
        //check if path exsit and create
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        //upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        //return the file
        return fileName;
    }
}
