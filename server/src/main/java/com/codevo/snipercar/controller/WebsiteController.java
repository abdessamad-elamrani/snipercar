package com.codevo.snipercar.controller;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.Date;
import java.util.Optional;

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
@RequestMapping("/api/website")
public class WebsiteController {

	@Autowired
	private WebsiteRepository websiteRepository;

	/**
	 * Read all websites for datatables
	 * 
	 * @param datatablesRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/datatables", method = RequestMethod.POST)
	public ResponseEntity<DatatablesResponse> readForDatatables(@RequestBody DatatablesRequest datatablesRequest) throws Exception {

		Pageable pageable = PageRequest.of(datatablesRequest.getStart() / datatablesRequest.getLength(), datatablesRequest.getLength());
		Page<Map<String, String>> data = websiteRepository.findAllForDatatables(pageable,
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
	 * Create website
	 * 
	 * @param website
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Website> create(@Valid @RequestBody Website website)
			throws Exception {
		
		return ResponseEntity.ok(websiteRepository.save(website));
	}

	/**
	 * Read website
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Website> read(@PathVariable(value = "id") Long id) throws Exception {

		Optional<Website> website = websiteRepository.findById(id);
		if (!website.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Website not found for this id :: " + id);
		}

		return ResponseEntity.ok(website.get());
	}

	/**
	 * Update website
	 * 
	 * @param id
	 * @param website
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Website> update(@PathVariable(value = "id") Long id, @Valid @RequestBody Website website)
			throws Exception {

		Optional<Website> websiteOrig = websiteRepository.findById(id);
		if (!website.getId().equals(id) || !websiteOrig.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Website not found for this id :: " + id);
		}

		return ResponseEntity.ok(websiteRepository.save(website));
	}
	
	/**
	 * Delete website
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity delete(@PathVariable(value = "id") Long id)
			throws Exception {
		
		Optional<Website> website = websiteRepository.findById(id);
		if (!website.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Website not found for this id :: " + id);
		}
		
		websiteRepository.deleteById(id);
		
        return ResponseEntity.ok().build();
	}
	
	/**
	 * Read all websites for select2
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/select2", method = RequestMethod.GET)
	public ResponseEntity<List<Website>> readForSelect2() throws Exception {

		return ResponseEntity.ok(websiteRepository.findAll());
	}

}
