package com.codevo.snipercar.controller;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import javassist.NotFoundException;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.codevo.snipercar.model.*;
import com.codevo.snipercar.repository.*;
import com.codevo.snipercar.component.*;
import com.codevo.snipercar.service.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

//import com.codevo.snipercar.exception.ResourceNotFoundException;
//import com.codevo.snipercar.model.Employee;
//import com.codevo.snipercar.repository.EmployeeRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	/**
	 * Read all admins for datatables
	 * 
	 * @param datatablesRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/datatables", method = RequestMethod.POST)
	public ResponseEntity<DatatablesResponse> readForDatatables(@RequestBody DatatablesRequest datatablesRequest)
			throws Exception {

		Pageable pageable = PageRequest.of(datatablesRequest.getStart() / datatablesRequest.getLength(), datatablesRequest.getLength());
		Page<Map<String, String>> data = userRepository.findAllAdminsForDatatables(pageable,
				datatablesRequest.getFilter().getOrDefault("name", ""));

		DatatablesResponse datatablesResponse = new DatatablesResponse();
		datatablesResponse.setData(data.getContent());
		datatablesResponse.setDraw(datatablesRequest.getDraw());
		datatablesResponse.setLength(datatablesRequest.getLength());
		datatablesResponse.setRecordsTotal(data.getTotalElements());
		datatablesResponse.setRecordsFiltered(data.getTotalElements());

		return ResponseEntity.ok(datatablesResponse);
	}

	/**
	 * Create admin
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<User> create(@Valid @RequestBody User user) throws Exception {

		if(user.getNewPassword().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty password");
		}
		if(!user.getNewPassword().equals(user.getNewPasswordConfirm())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new password mismatch " + user.getNewPassword() + "!=" + user.getNewPasswordConfirm());
		}
		user.setPassword(bcryptEncoder.encode(user.getNewPassword()));
		user.setCompany(null);
		
		return ResponseEntity.ok(userRepository.save(user));
	}

	/**
	 * Read admin
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> read(@PathVariable(value = "id") Long id) throws Exception {

//		Pageable pageable = PageRequest.of(0, 1);
		Optional<User> user = userRepository.findAdminById(id);
		if (!user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for this id :: " + id);
		}

		return ResponseEntity.ok(user.get());
	}

	/**
	 * Update admin
	 * 
	 * @param id
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<User> update(@PathVariable(value = "id") Long id, @Valid @RequestBody User user)
			throws Exception {

		Optional<User> OptionalUser = userRepository.findAdminById(id);
		if (!OptionalUser.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for this id :: " + id);
		}
		if (!user.getId().equals(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id mismatch " + user.getId() + "!=" + id);
		}

		User userOrig = OptionalUser.get();
		userOrig.setName(user.getName());
		userOrig.setEmail(user.getEmail());
		userOrig.setPhone(user.getPhone());
		userOrig.setRole(user.getRole());
		userOrig.setActive(user.getActive());
		userOrig.setUsername(user.getUsername());
		if (user.getPasswordChange()) {
			if(user.getNewPassword().isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty password");
			}
			if(!user.getNewPassword().equals(user.getNewPasswordConfirm())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new password mismatch " + user.getNewPassword() + "!=" + user.getNewPasswordConfirm());
			}
			userOrig.setPassword(bcryptEncoder.encode(user.getNewPassword()));
		}

		return ResponseEntity.ok(userRepository.save(userOrig));
	}

	/**
	 * Delete admin
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity delete(@PathVariable(value = "id") Long id) throws Exception {

		Optional<User> user = userRepository.findAdminById(id);
		if (!user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for this id :: " + id);
		}

		userRepository.deleteById(id);

		return ResponseEntity.ok().build();
	}

	/**
	 * Read all admins for select2
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/select2", method = RequestMethod.GET)
	public ResponseEntity<List<User>> readForSelect2() throws Exception {

		return ResponseEntity.ok(userRepository.findAll());
	}

	/**
	 * Read all admins' usernames
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/reservedUsernames/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<String>> readReservedUsernames(@PathVariable(value = "id") Long id) throws Exception {

		return ResponseEntity.ok(userRepository.findAllReservedUsernames(id));
	}

}
