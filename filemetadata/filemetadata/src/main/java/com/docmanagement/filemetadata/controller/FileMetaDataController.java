package com.docmanagement.filemetadata.controller;

import com.docmanagement.filemetadata.entity.FileMetaData;
import com.docmanagement.filemetadata.request.FileRequest;
import com.docmanagement.filemetadata.service.FileMetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/metadata")
public class FileMetaDataController {

    @Autowired
    private FileMetaDataService fileMetaDataService;

    @PostMapping("/adddata")
    public ResponseEntity<String> addMetaData(@RequestBody FileMetaData fileMetaData) {
        String responseMessage = fileMetaDataService.addMetaData(fileMetaData);
        
        if (responseMessage.contains("already exists")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMessage);
        }
        
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/getdata")
    public ResponseEntity<FileMetaData> getMetaData(@RequestBody String fileId) {
        Optional<FileMetaData> metadata = fileMetaDataService.getMetaDataByFileId(fileId);
        
        return metadata.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/getFile")
    public ResponseEntity<FileMetaData> getFileByUsernameAndFileName(@RequestBody FileRequest fileRequest) {
        Optional<FileMetaData> fileMetaData = fileMetaDataService.getFileByUsernameAndFileName(fileRequest);
        
        return fileMetaData.map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/getFiles/{username}")
    public ResponseEntity<?> getFilesByUsername(@PathVariable String username) {
        List<FileMetaData> files = fileMetaDataService.getFilesByUsername(username);

        if (files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No files found for the user.");
        }

        return ResponseEntity.ok(files.stream()
                                      .map(file -> Map.of("fileName", file.getFileName(), "fileId", file.getFileId())));
    }
}
