package com.ajeet.backEndAPI.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ajeet.backEndAPI.Entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	UserEntity findByUsername(String username);
}
