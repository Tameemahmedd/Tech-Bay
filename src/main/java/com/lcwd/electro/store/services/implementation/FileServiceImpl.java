package com.lcwd.electro.store.services.implementation;

import com.lcwd.electro.store.exceptions.BadApiRequestException;
import com.lcwd.electro.store.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        log.info("Filename :{}",originalFilename);
        String filename= UUID.randomUUID().toString();
        String extension=originalFilename.substring(originalFilename.lastIndexOf("."));
    String fileNameWithExtension=filename+extension;
    String fullPathWithFileName=path+fileNameWithExtension;

    log.info("Full image path : {}",fullPathWithFileName);

    if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {

        log.info("file extension is:{}",extension);
        File folder=new File(path);
        if(!folder.exists()){
            //create folder
            folder.mkdirs();
        }
//upload
        Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
return fileNameWithExtension;
    }else
    {throw new BadApiRequestException("File with extension"+extension+" not allowed.");}
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
    String fullpath=path+File.separator+name;
    InputStream inputStream=new FileInputStream(fullpath);
    return inputStream;
    }
}
