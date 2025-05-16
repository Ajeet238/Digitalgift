package com.ajeet.backEndAPI.payload;

import java.util.Set;

import com.ajeet.backEndAPI.Entity.CommentEntity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

//@Data
public class PostEntityDto {

	private Long id;
	
	@NotEmpty
	@Size(min=2, message = "post title must have atleast 2 character")
	private String title;
	
	@NotEmpty
	private String content;
	
	@NotEmpty
	@Size(min=10, message = "post title must have atleast 10 character")
	private String description;
	
	private Set<CommentEntityDto> comments;
	
	public PostEntityDto(Long id, String title, String content, String description, Set<CommentEntityDto> comments) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.description = description;
		this.comments = comments; 
	}
	public PostEntityDto() {
		super();
	}
	
	public Long getId() {
		return id;
	}
	public Set<CommentEntityDto> getComments() {
		return comments;
	}
	public void setComments(Set<CommentEntityDto> comments) {
		this.comments = comments;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public String getDescription() {
		return description;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

	
	
}
