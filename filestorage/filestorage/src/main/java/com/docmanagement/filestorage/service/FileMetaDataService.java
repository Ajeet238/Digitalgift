package com.docmanagement.filestorage.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.docmanagement.filestorage.DTO.FileMetaDataDTO;
import com.docmanagement.filestorage.request.FileRequest;

// used to call API inside microservices
@FeignClient(name = "filemetadata",path = "/metadata")
public interface FileMetaDataService {
	
	@PostMapping("/adddata")
	ResponseEntity<String> addMetaData(FileMetaDataDTO values);
	
	@PostMapping("/getFile")
	 ResponseEntity<FileMetaDataDTO> getFileByUsernameAndFileName(@RequestBody FileRequest fileRequest );
	
	
}
