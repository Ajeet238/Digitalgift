package com.ajeet.backEndAPI.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntityDto {
	private long id;
	@NotEmpty
	@Size(min = 3, message = "Name should be atleast 3 character")
	private String name;
	
	@NotEmpty(message = "Email should not be null")
	@Email
	private String email;
	
	@NotEmpty
	@Size(min = 10, message = "Body should be atleast 10 character")
	private String body;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	
}
