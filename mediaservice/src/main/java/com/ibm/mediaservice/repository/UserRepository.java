package com.ibm.mediaservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibm.mediaservice.models.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
	
	Optional<User> findByUsername(String username);
}
