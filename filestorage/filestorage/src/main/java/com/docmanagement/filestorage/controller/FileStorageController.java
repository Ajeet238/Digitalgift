package com.docmanagement.filestorage.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.docmanagement.filestorage.DTO.FileMetaDataDTO;
import com.docmanagement.filestorage.request.FileRequest;
import com.docmanagement.filestorage.service.AuthUserService;
import com.docmanagement.filestorage.service.FileMetaDataService;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@RestController
@RequestMapping("/storage")
public class FileStorageController {

    private final FileMetaDataService fileMetaDataService;

    public FileStorageController(FileMetaDataService fileMetaDataService) {
        this.fileMetaDataService = fileMetaDataService;
    }

    @Autowired
    private AuthUserService authService;
    
	@Autowired
	private S3Client s3client;
	
	@Value("${aws.s3.bucket.name}")
	private String bucketName;
	
	// @PreAuthorize("hasRole('USER')")
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestPart("username") String username, HttpServletRequest request) {
		
	    String authHeader = request.getHeader("Authorization");
	    
		 if (authHeader == null || !authHeader.startsWith("Bearer ")) {
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
		    }

		    // Extract the JWT token by removing the "Bearer " prefix
		    String jwtToken = authHeader.substring(7);

		
	    String authUser;
	    try {
	        ResponseEntity<String> res = authService.getAuthenticationStatus(authHeader);
	        authUser = res.getBody();
	    } catch (FeignException e) {
	    	System.out.println(e);
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed"+ e);
	    }
		
		if(!authUser.equals(username)) {
			 throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid user name in request data.");
		}
		
		  // Generate a unique file name using the username and timestamp
	    String originalFileName = file.getOriginalFilename();
	    String uniqueFileName = username + "/" + System.currentTimeMillis() + "_" + originalFileName;
		String url = "https://s3.amazonaws.com/ajeetaws/" + uniqueFileName;

		// upload metadata
		try {
			 PutObjectResponse putObjectResponse = s3client.putObject(PutObjectRequest.builder().bucket(bucketName).key(uniqueFileName).build(),
					RequestBody.fromBytes(file.getBytes()));
			
			// 2. Create metadata
			 String fileId = generateFileId();
			FileMetaDataDTO metaData1 = new FileMetaDataDTO();
			metaData1.setFileName(originalFileName);
			metaData1.setUniqueFileName(uniqueFileName);
			metaData1.setUrl(url);
			metaData1.setFileSize(file.getSize());
			metaData1.setFileType(file.getContentType());
			metaData1.setUserName(username);
			metaData1.setFileId(fileId);
			//metaData1.setFileId(versionId);

			// 3. Send metadata to Metadata Service using Feign Client
			ResponseEntity<String> response = fileMetaDataService.addMetaData(metaData1);

			if (response.getStatusCode().is2xxSuccessful()) {
				return ResponseEntity.ok("File uploaded successfully with URL: " + url);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Metadata upload failed.");
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
		}
	}
	
	@PostMapping("/download")
	// @PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> downloadFile(@org.springframework.web.bind.annotation.RequestBody FileRequest fileRequest){
		
		String userName = fileRequest.getUserName();
		String fileName = fileRequest.getFileName();
		ResponseEntity<FileMetaDataDTO> fileMetaData	= fileMetaDataService.getFileByUsernameAndFileName(fileRequest);
		String uniqueFileName = fileMetaData.getBody().getUniqueFileName();
		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(bucketName)
				.key(uniqueFileName)
				.build();
		 
		//.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")  => help to download the file in required format
		byte [] content = s3client.getObjectAsBytes(getObjectRequest).asByteArray();
		// below code return array of bytes. If you save the response in postman in save as file the file get downloaded.
		//		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
		//				.contentType(MediaType.APPLICATION_OCTET_STREAM)
		//				.body(content);
		
		// return the array of bytes in base64 json format.
		String base64 = Base64.getEncoder().encodeToString(content);
		
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"fileContent\": \"" + base64 + "\"}");
	}
	
	 public static String generateFileId() {
	        // Current timestamp in milliseconds
//	        long timestamp = System.currentTimeMillis();
//	       
//	        // Generate a random number (you can adjust the range)
//	        Random random = new Random();
//	        long randomValue = random.nextInt(1000000);  // Random value between 0 and 999,999
//	        
//	        // Combine timestamp and random value
//	        return timestamp * 1000000 + randomValue;  // Combine them to create a unique long ID
		 return UUID.randomUUID().toString();
	    }
	    
	   
	
}
