package com.docmanagement.filemetadata.entity;

import java.util.Date;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FileMetaData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    private Long fileId;
    private String fileName;
    private String ownerId;
    private Long fileSize;
    private Date uploadTime;
    
    
	public FileMetaData() {
		super();
	}
	
	public FileMetaData(Long id, Long fileId, String fileName, String ownerId, Long fileSize,
			Date uploadTime) {
		super();
		this.id = id;
		this.fileId = fileId;
		this.fileName = fileName;
		this.ownerId = ownerId;
		this.fileSize = fileSize;
		this.uploadTime = uploadTime;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
    
}
