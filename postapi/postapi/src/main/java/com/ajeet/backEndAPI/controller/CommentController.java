package com.ajeet.backEndAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.ajeet.backEndAPI.Services.CommentService;
import com.ajeet.backEndAPI.payload.CommentEntityDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CommentController {
	
	@Autowired
	CommentService commentService;
// Add Comment to post
	@PostMapping("/post/{post_id}/comment")
	ResponseEntity<CommentEntityDto> createComment( @Valid @RequestBody CommentEntityDto commentEntityDto, @PathVariable(value = "post_id") Long id){
		
		CommentEntityDto comment =	commentService.createComment(commentEntityDto,id);
		return new ResponseEntity<>(comment, HttpStatus.CREATED);
	
	}
// get comment by post id
	@GetMapping("/comment/{post_id}")
	ResponseEntity<List<CommentEntityDto>> getCommentById(@PathVariable(value = "post_id") Long id){
		List<CommentEntityDto> comments = commentService.getCommentById(id);
		return new ResponseEntity<>(comments, HttpStatus.CREATED);
	}
	
// get particular comment of particular post;
	@GetMapping("/comment/{commentId}/post/{postId}")
	ResponseEntity<CommentEntityDto> getCommentOfPost(@PathVariable(name = "commentId") Long commentId, @PathVariable(name = "postId") Long postId){
		CommentEntityDto comment = commentService.getCommentByPostId(commentId, postId);
		return new ResponseEntity<>(comment,HttpStatus.OK);
	}
// update particular comment of particular post;
	@PostMapping("/post/{postId}/update/{commentId}")
	ResponseEntity<CommentEntityDto> updateComment(@Valid @PathVariable(name = "postId") Long postId,@PathVariable(name = "commentId")Long commentId, @RequestBody CommentEntityDto commentEntityDto){
		CommentEntityDto comment = commentService.updateComment(postId, commentId, commentEntityDto);
		return new ResponseEntity<>(comment, HttpStatus.CREATED);
	}
	// delete particular comment of particular post;
	@DeleteMapping("/post/{postId}/comment/{commentId}")
	ResponseEntity<String> deleteComment(@PathVariable(name = "postId") Long postId,@PathVariable(name = "commentId") Long commentId){
		commentService.deleteComment(postId, commentId);
		return new ResponseEntity<>("Comment Deleted Successfull", HttpStatus.OK);
	}
}
