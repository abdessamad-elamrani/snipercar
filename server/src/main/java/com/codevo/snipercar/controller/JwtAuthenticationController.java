package com.codevo.snipercar.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codevo.snipercar.config.JwtTokenUtil;
import com.codevo.snipercar.model.JwtRequest;
import com.codevo.snipercar.model.JwtResponse;
import com.codevo.snipercar.model.User;
import com.codevo.snipercar.repository.UserRepository;
import com.codevo.snipercar.service.JwtUserDetailsService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private UserRepository UserRepository;

	/**
	 * Generate jwt token
	 * 
	 * @param authenticationRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/token/generate", method = RequestMethod.POST)
	public ResponseEntity<?> generateToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		final Authentication authentication = authenticate(authenticationRequest.getUsername(),
				authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);
		final String tokenRefresh = null;
		final Long expiration = jwtTokenUtil.getExpirationDateFromToken(token).getTime() / 1000;
		final Set roles = new HashSet<String>();
		authentication.getAuthorities().forEach(authority -> {
			roles.add(authority.toString());
		});
		final User user = UserRepository.findByUsername(authentication.getName());
		
		return ResponseEntity.ok(new JwtResponse(token, tokenRefresh, expiration, roles, user));
	}

	/**
	 * Refresh/regenerate jwt token
	 * 
	 * @param request
	 * @param authentication
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/token/refresh", method = RequestMethod.POST)
	public ResponseEntity<?> refreshToken(HttpServletRequest request, Authentication authentication) throws Exception {

		final String requestTokenHeader = request.getHeader("Authorization");
		final String token = requestTokenHeader.substring(7);

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
		final String tokenRefresh = jwtTokenUtil.generateToken(userDetails);

		final Long expiration = jwtTokenUtil.getExpirationDateFromToken(tokenRefresh).getTime() / 1000;
		final Set roles = new HashSet<String>();
		authentication.getAuthorities().forEach(authority -> {
			roles.add(authority.toString());
		});
		final User user = UserRepository.findByUsername(authentication.getName());
		
		return ResponseEntity.ok(new JwtResponse(token, tokenRefresh, expiration, roles, user));
	}

	/**
	 * Create user
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
		return ResponseEntity.ok(userDetailsService.save(user));
	}

	/**
	 * Authenticate user
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private Authentication authenticate(String username, String password) throws Exception {
		try {
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}