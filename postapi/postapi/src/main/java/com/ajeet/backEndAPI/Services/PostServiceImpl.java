package com.ajeet.backEndAPI.Services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ajeet.backEndAPI.Entity.PostEntity;
import com.ajeet.backEndAPI.Exception.ResourceNotFoundException;
import com.ajeet.backEndAPI.Repository.PostRepository;
import com.ajeet.backEndAPI.Services.PostService;
import com.ajeet.backEndAPI.payload.CommentEntityDto;
import com.ajeet.backEndAPI.payload.PostEntityDto;
import com.ajeet.backEndAPI.payload.PostResponse;

@Service
public class PostServiceImpl implements PostService {
	
	private PostRepository postRepository;
	private ModelMapper modelMapper;
	
	PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper){
		this.postRepository = postRepository;
		this.modelMapper = modelMapper;
	}
	//@Autowired
	
	
	@Override
	public PostEntityDto createPost(PostEntityDto postEntityDto) {
		
		// convert PostEntityDto to PostEntity
		
		PostEntity postEntity = maptoPostEntity(postEntityDto);
//		postEntity.setTitle(postEntityDto.getTitle());
//		postEntity.setContent(postEntityDto.getContent());
//		postEntity.setDescription(postEntityDto.getDescription());
		
		PostEntity newPost = postRepository.save(postEntity);
		//  postRepository.save(postEntity) return PostEntity so need to convert it to PostEntityDto
		// convert PostEntity to PostEntityDto
		PostEntityDto postResponse = maptoPostEntityDto(postEntity);
//		postResponse.setId(newPost.getId());
//		postResponse.setTitle(newPost.getTitle());
//		postResponse.setContent(newPost.getContent());
//		postResponse.setDescription(newPost.getDescription());
		
		return postResponse;

		
	}
	
	// getALLPost with pagination and sort
	
	public PostResponse getAllPost(int pageNO, int pageSize, String sortBy, String sortDir){
	//	Pageable pageable = PageRequest.of(pageNO, pageSize);
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
		System.out.println(Sort.Direction.ASC.name() + "hii");
	//	Pageable pageable = PageRequest.of(pageNO, pageSize, Sort.by(sortBy)); => asc desc not considered.
		
		Pageable pageable = PageRequest.of(pageNO, pageSize, sort);
		
		Page<PostEntity> posts = postRepository.findAll(pageable);
		
		// get content from page object
		List<PostEntity> listOfPosts = posts.getContent();
		List <PostEntityDto> content = 	listOfPosts.stream().map(PostEntity -> maptoPostEntityDto(PostEntity)).collect(Collectors.toList());
		PostResponse postResponse = new PostResponse(content, pageSize, pageSize, pageSize, pageSize, false);
		
		postResponse.setContent(content);
		postResponse.setPageNo(posts.getNumber());
		postResponse.setPageSize(posts.getSize());
		postResponse.setTotalElements(posts.getTotalElements());
		postResponse.setTotalPages(posts.getTotalPages());
		postResponse.setLast(posts.isLast());
		
		return postResponse;
		
	}
	
//	// getallpost without pagination and sorting
//	public List<PostEntityDto> getAllPost(){
//		List<PostEntity> posts = postRepository.findAll();
//	return	posts.stream().map(PostEntity -> maptoPostEntityDto(PostEntity)).collect(Collectors.toList());
//		
//	}
	public PostEntityDto getPostBYId(Long id) {
		// find by id return optional class which has many inbuilt method
		PostEntity post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post","id",id));
		return maptoPostEntityDto(post);
		
	}
	
	
	public PostEntityDto updatePost(PostEntityDto postEntityDto, Long id) {
		PostEntity oldPost = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("post","id",id) );
		oldPost.setTitle(postEntityDto.getTitle());
		oldPost.setContent(postEntityDto.getContent());
		oldPost.setDescription(postEntityDto.getDescription());
		postRepository.save(oldPost);
		PostEntityDto updatedPost = maptoPostEntityDto(oldPost);
		
		return updatedPost;
		
	}
	
	public void deletePost(Long id) {
		postRepository.deleteById(id);
	}
	
	
	private PostEntityDto maptoPostEntityDto(PostEntity postEntity) {
		PostEntityDto postEntityDto = modelMapper.map(postEntity, PostEntityDto.class);
//		postEntityDto.setId(postEntity.getId());
//		postEntityDto.setTitle(postEntity.getTitle());
//		postEntityDto.setContent(postEntity.getContent());
//		postEntityDto.setDescription(postEntity.getDescription());
		
		return postEntityDto;
	}
	private PostEntity maptoPostEntity(PostEntityDto postEntityDto) {
		
		PostEntity postEntity = modelMapper.map(postEntityDto, PostEntity.class);
//		postEntity.setId(postEntityDto.getId());
//		postEntity.setTitle(postEntityDto.getTitle());
//		postEntity.setContent(postEntityDto.getContent());
//		postEntity.setDescription(postEntityDto.getDescription());
		
		return postEntity;
	}



	
}
