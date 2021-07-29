package com.ibm.mediaservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ibm.mediaservice.models.User;

public interface UserService extends UserDetailsService{

	User findByEmail(String email);
	
}
