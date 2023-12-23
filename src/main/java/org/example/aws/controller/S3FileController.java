package org.example.aws.controller;

import org.example.aws.service.S3FileOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class S3FileController {
    @Autowired
    private S3FileOperationService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value="file")MultipartFile multipartFile){
        String uploadResp = fileService.uploadFile(multipartFile);
        return new ResponseEntity<>(uploadResp, HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName){
        try {
           byte[] content = fileService.downloadFile(fileName);
            ByteArrayResource resource = new ByteArrayResource(content);

            return ResponseEntity.ok()
                    .contentLength(content.length)
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName){
        return new ResponseEntity<>(fileService.deleteFile(fileName), HttpStatus.OK);
    }
}
