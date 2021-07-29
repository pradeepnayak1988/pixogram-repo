package com.ibm.mediaservice.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ibm.mediaservice.models.Role;
import com.ibm.mediaservice.models.User;
import com.ibm.mediaservice.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
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
