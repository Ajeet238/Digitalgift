package com.ajeet.docManagement.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ajeet.docManagement.Entity.Token;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
	
	
    Optional<Token> findByUsername(String username);
    Optional<Token> findByEmail(String email);
    Optional<Token> findByPhone(String phone);

	Token findByToken(String jwt);
	
//    @Modifying
//    @Transactional
//    @Query("UPDATE Token t SET t.isRevoked = true WHERE t.token = :token")
//    void revokeTokenByToken(String token);
}

