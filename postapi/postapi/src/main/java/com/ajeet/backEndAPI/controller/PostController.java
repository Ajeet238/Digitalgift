package com.ajeet.backEndAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ajeet.backEndAPI.Services.PostService;
import com.ajeet.backEndAPI.Util.AppConstants;
import com.ajeet.backEndAPI.payload.PostEntityDto;
import com.ajeet.backEndAPI.payload.PostResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@PostMapping("/post")
	ResponseEntity<PostEntityDto> createPost(@Valid @RequestBody PostEntityDto postEntityDto) {
		return new ResponseEntity<>( postService.createPost(postEntityDto), HttpStatus.CREATED);
	}
	// @RequestParam(value = "pageNo",defaultValue = "0", required = false) int pageNo,
	// @RequestParam(value = "pageSize",defaultValue = "10", required = false) int pageSize => these two argument are given for sorting and pagination.
	// remove this if you don't want pagination and sorting
	
	@GetMapping("/posts")
	ResponseEntity<PostResponse> getAllPost( @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value ="sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
			){
		return new ResponseEntity<>(postService.getAllPost(pageNo, pageSize, sortBy, sortDir),HttpStatus.OK );
	}
	@GetMapping("/{id}")
	ResponseEntity<PostEntityDto> getPostById(@PathVariable(name = "id") Long id){
		return ResponseEntity.ok(postService.getPostBYId(id) );
	}
	@PutMapping("/{id}")
	ResponseEntity<PostEntityDto> updatePostById(@PathVariable(name = "id") Long id, @RequestBody PostEntityDto postEntityDto ){
		return ResponseEntity.ok(postService.updatePost(postEntityDto, id) );
	}
	
	@DeleteMapping("/{id}")
	ResponseEntity<String> deletepostById(@PathVariable(name = "id") Long id) {
		postService.deletePost(id);
		return new ResponseEntity<>("Post Deled Successfull", HttpStatus.OK);
	}
	
}
