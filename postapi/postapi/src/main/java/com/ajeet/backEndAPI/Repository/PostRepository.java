package com.ajeet.backEndAPI.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeet.backEndAPI.Entity.PostEntity;

// to make postRepository JPA extends JPARepository<class,PK>
// need to make seperate repository for each entity class

public interface PostRepository extends JpaRepository<PostEntity, Long> {
	
}
