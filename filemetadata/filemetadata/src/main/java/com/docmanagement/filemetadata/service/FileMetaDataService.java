package com.docmanagement.filemetadata.service;

import java.util.List;
import java.util.Optional;

import com.docmanagement.filemetadata.entity.FileMetaData;
import com.docmanagement.filemetadata.request.FileRequest;


public interface FileMetaDataService {
	 String addMetaData(FileMetaData fileMetaData);

	    Optional<FileMetaData> getMetaDataByFileId(String fileId);

	    Optional<FileMetaData> getFileByUsernameAndFileName(FileRequest fileRequest);

	    List<FileMetaData> getFilesByUsername(String username);
}
