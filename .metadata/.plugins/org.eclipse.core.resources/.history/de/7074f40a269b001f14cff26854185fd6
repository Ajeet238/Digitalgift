package com.ajeet.backEndAPI.Services;

import java.util.List;

import com.ajeet.backEndAPI.Entity.CommentEntity;
import com.ajeet.backEndAPI.payload.CommentEntityDto;

public interface CommentService {
	CommentEntityDto createComment (CommentEntityDto commentEntityDto, Long id);
	List<CommentEntityDto> getCommentById(Long id);
	CommentEntityDto getCommentByPostId(Long commentId, Long PostId);
	CommentEntityDto updateComment(Long postId,Long commentId, CommentEntityDto commentDto);
	void deleteComment(Long postId, Long commentId);
}
