package com.ajeet.backEndAPI.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @responsestatus will return status code whenever exception is occured
@ResponseStatus(value = HttpStatus.NOT_FOUND)

// must extend runtimeexception. When exception occured at runtime this class will show specific message
public class ResourceNotFoundException extends RuntimeException{

	private String resourcename;
	private String fieldname;
	private Long fieldvalue;
	
	public ResourceNotFoundException(String resourcename,String fieldname, Long fieldvalue ) {
		
		super(String.format( "%s not found with %s : %s" , resourcename, fieldname, fieldvalue));
		// resourcename not found with fieldname : fieldvalue
		
		this.fieldname = fieldname;
		this.fieldvalue = fieldvalue;
		this.resourcename = resourcename;
	}
	
	public String getResourcename() {
		return resourcename;
	}

	public String getFieldname() {
		return fieldname;
	}

	public Long getFieldvalue() {
		return fieldvalue;
	}

	
	
}
