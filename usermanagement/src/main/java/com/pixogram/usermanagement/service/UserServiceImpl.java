package com.pixogram.usermanagement.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pixogram.usermanagement.models.Role;
import com.pixogram.usermanagement.models.User;
import com.pixogram.usermanagement.repository.UserRepository;
import com.pixogram.usermanagement.web.dto.UserRegistrationDto;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User save(UserRegistrationDto registration) {
		User user = new User();
		user.setFirstName(registration.getFirstName());
		user.setLastName(registration.getLastName());
		user.setEmail(registration.getEmail());
		user.setUsername(registration.getUsername());
		user.setPassword(passwordEncoder.encode(registration.getPassword()));
		user.setProfilePictureUrl(registration.getProfilePictureUrl());
		registration.setCreatedOn(Calendar.getInstance().getTime());
		user.setCreatedOn(registration.getCreatedOn());
		registration.setActive(true);
		user.setActive(registration.isActive());
		user.setRoles(Arrays.asList(new Role("ROLE_USER")));
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(email);
		
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		 
		//return new org.springframework.security.core.userdetails.User(user.getEmail(),
		//		user.getPassword(),
		//		mapRolesToAuthorities(user.getRoles()));
		
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), 
				user.isActive(), true, true, true, mapRolesToAuthorities(user.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}
	
}
