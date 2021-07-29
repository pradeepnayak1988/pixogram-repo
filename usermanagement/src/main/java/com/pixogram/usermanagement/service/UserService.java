package com.pixogram.usermanagement.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.pixogram.usermanagement.models.User;
import com.pixogram.usermanagement.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService{

	User findByEmail(String email);
	
	User save(UserRegistrationDto registration);
}
