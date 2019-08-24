package com.codevo.snipercar.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codevo.snipercar.repository.UserRepository;
import com.codevo.snipercar.model.User;

@Service
@Transactional
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository UserRepository;
	@PersistenceContext
	EntityManager em;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = UserRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		
		Set roles = new HashSet<String>();
		roles.add("ROLE_" + user.getRole());
		if (roles.contains("ROLE_SUPER_ADMIN")) {
			roles.add("ROLE_ADMIN");
		} else if (roles.contains("ROLE_SUPER_AGENT")) {
			roles.add("ROLE_AGENT");
		}
		roles.add("ROLE_USER");
		Set authorities = new HashSet<>();
		roles.forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.toString()));
		});
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				authorities);
	}

	public User save(User user) {
		user.setPassword(bcryptEncoder.encode(user.getPassword()));
		em.persist(user);
		return user;
	}
}