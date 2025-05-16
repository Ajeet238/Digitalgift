package com.docmanagement.filemetadata.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.docmanagement.filemetadata.entity.FileMetaData;
import com.docmanagement.filemetadata.request.FileRequest;

@Repository
public interface FileMetaDataRepository extends JpaRepository<FileMetaData, Long> {
	
	FileMetaData findByFileId(String fileId);

	List<FileMetaData> findByUserName(String username);

	Optional<FileMetaData> findByUserNameAndFileName(String userName, String fileName);

	
}
