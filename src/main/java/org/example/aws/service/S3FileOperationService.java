package org.example.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class S3FileOperationService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3Client s3Client;

    public String uploadFile(MultipartFile multipartFile){
        File fileObj = convertMultiPartFileToFile(multipartFile);
        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest( bucketName, fileName, fileObj));
        fileObj.delete();
        return "File uploaded: "+ fileName;
    }

    public byte[] downloadFile(String fileName) throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        byte[] content = IOUtils.toByteArray(s3ObjectInputStream);

        return  content;
    }

    public String deleteFile(String fileName){
        s3Client.deleteObject(bucketName, fileName);
        return fileName +" is removed..";
    }

    private File convertMultiPartFileToFile(MultipartFile multipartFile) {
        File convertedFile =new File(multipartFile.getOriginalFilename());
        try{
            FileOutputStream outputStream = new FileOutputStream(convertedFile);
        } catch (FileNotFoundException e) {
            log.error("Error while converting from multipart file to File");
            throw new RuntimeException(e);
        }
        return convertedFile;
    }
}
