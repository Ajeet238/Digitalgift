package com.ajeet.backEndAPI.Exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ajeet.backEndAPI.payload.ErrorDetailClass;

@ControllerAdvice  // used to handle exception globally
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// handle specific exception
	// handle global exception
	@ExceptionHandler(ResourceNotFoundException.class)
	ResponseEntity<ErrorDetailClass>  handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest){
		ErrorDetailClass errorDetails = new  ErrorDetailClass(new Date(),exception.getMessage(), webRequest.getDescription(false) );
		
		return new ResponseEntity<>(errorDetails, HttpStatus.OK);
}
	
// handle blogAPI exception	
	@ExceptionHandler(BlogApiException.class)
	ResponseEntity<ErrorDetailClass>  handleBlogApiException(ResourceNotFoundException exception, WebRequest webRequest){
		ErrorDetailClass errorDetails = new  ErrorDetailClass(new Date(),exception.getMessage(), webRequest.getDescription(false) );
		
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
// handle global exception
// Exception class is base class of all Exception hierarchical class. So any exception come that belongs to exception class will be handled here
	
	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorDetailClass>  handleGlobalException(Exception exception, WebRequest webRequest){
		ErrorDetailClass errorDetails = new  ErrorDetailClass(new Date(),exception.getMessage(), webRequest.getDescription(false) );
		
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
// handle validation response approch 1
		@Override
		protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
				HttpHeaders headers, HttpStatusCode status, WebRequest request) {
			
			Map<String,String> errors = new HashMap<>();
			
			ex.getBindingResult().getAllErrors().forEach((error)->{
				String fieldName = ((FieldError)error).getField();
				String message = error.getDefaultMessage();
				
				errors.put(fieldName,message);
			});
			
			return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
		}
//		// handle validation response approch 2 => giving error
//	
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<Object>  handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest webRequest){
////		ErrorDetailClass errorDetails = new  ErrorDetailClass(new Date(),exception.getMessage(), webRequest.getDescription(false) );
//		Map<String,String> errors = new HashMap<>();
//		
//		exception.getBindingResult().getAllErrors().forEach((error)->{
//			String fieldName = ((FieldError)error).getField();
//			String message = error.getDefaultMessage();
//			
//			errors.put(fieldName,message);
//		});
//		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//	}
}
