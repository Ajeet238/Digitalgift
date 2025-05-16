package com.docmanagement.filemetadata.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.docmanagement.filemetadata.entity.FileMetaData;
import com.docmanagement.filemetadata.repository.FileMetaDataRepository;
import com.docmanagement.filemetadata.request.FileRequest;
import com.docmanagement.filemetadata.service.FileMetaDataService;

@Service
public class FileMetaDataServiceImpl implements FileMetaDataService {

	 @Autowired
	    private FileMetaDataRepository fileMetaDataRepository;

	    @Override
	    public String addMetaData(FileMetaData fileMetaData) {
	        try {
	            // Validate username and file name combination
	            Optional<FileMetaData> existingFile = fileMetaDataRepository.findByUserNameAndFileName(
	                    fileMetaData.getUserName(), fileMetaData.getFileName());

	            if (existingFile.isPresent()) {
	                return "File with the same name already exists for this user.";
	            }

	            fileMetaDataRepository.save(fileMetaData);
	            return "Metadata saved successfully.";

	        } catch (DataIntegrityViolationException e) {
	            return "Data integrity violation, possibly duplicate file.";
	        } catch (Exception e) {
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", e);
	        }
	    }

	    @Override
	    public Optional<FileMetaData> getMetaDataByFileId(String fileId) {
	        return Optional.ofNullable(fileMetaDataRepository.findByFileId(fileId));
	    }

	    @Override
	    public Optional<FileMetaData> getFileByUsernameAndFileName(FileRequest fileRequest) {
	        String fileName = fileRequest.getFileName();
	        String userName = fileRequest.getUserName();
	        return fileMetaDataRepository.findByUserNameAndFileName(userName, fileName);
	    }

	    @Override
	    public List<FileMetaData> getFilesByUsername(String username) {
	        return fileMetaDataRepository.findByUserName(username);
	    }
	}
