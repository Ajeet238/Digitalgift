package com.ajeet.backEndAPI.Services;

import java.util.List;

import com.ajeet.backEndAPI.payload.CommentEntityDto;
import com.ajeet.backEndAPI.payload.PostEntityDto;
import com.ajeet.backEndAPI.payload.PostResponse;

public interface PostService {
	
	PostEntityDto createPost(PostEntityDto postEntityDto);
	PostResponse getAllPost(int pageNO, int pageSize, String sortBy, String sortDir);
	PostEntityDto getPostBYId(Long id);
	PostEntityDto updatePost(PostEntityDto postEntityDto, Long id);
	void deletePost(Long id);

	
}
