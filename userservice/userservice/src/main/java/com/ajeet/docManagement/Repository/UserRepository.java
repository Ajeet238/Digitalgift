package com.ajeet.docManagement.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ajeet.docManagement.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmail(String email);	
    
    @Query("SELECT u FROM user_table u WHERE Lower(u.username) = Lower(:input) OR Lower(u.email) = Lower(:input)")
    Optional<User> findByUsernameOrEmail(@Param(value = "input") String usernameOrEmail);
}

