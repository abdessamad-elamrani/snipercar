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
import org.springframework.http.MediaType;
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
@RequestMapping("/api/selection")
public class SelectionController {

	@Autowired
	private SelectionRepository selectionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Read all selections for datatables
	 * 
	 * @param datatablesRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/datatables", method = RequestMethod.POST)
	public ResponseEntity<DatatablesResponse> readForDatatables(@RequestBody DatatablesRequest datatablesRequest) throws Exception {

		Pageable pageable = PageRequest.of(datatablesRequest.getStart() / datatablesRequest.getLength(), datatablesRequest.getLength());
		Long userId = new Long(0);
		try{
			userId = Long.parseLong(datatablesRequest.getFilter().getOrDefault("userId", "0"));
		} catch (Exception e) {
			//do nothing
		}
		Page<Map<String, String>> data;
		if(userId > 0) {
			data = selectionRepository.findAllForDatatables(pageable,
					datatablesRequest.getFilter().getOrDefault("name", ""),
					userId);
		} else {
			data = selectionRepository.findAllForDatatables(pageable,
					datatablesRequest.getFilter().getOrDefault("name", ""));
		}

		DatatablesResponse datatablesResponse = new DatatablesResponse();
		datatablesResponse.setData(data.getContent());
		datatablesResponse.setDraw(datatablesRequest.getDraw());
		datatablesResponse.setLength(datatablesRequest.getLength());
		datatablesResponse.setRecordsTotal(data.getTotalElements());
		datatablesResponse.setRecordsFiltered(data.getTotalElements());

		return ResponseEntity.ok(datatablesResponse);
	}
	
	/**
	 * Create selection
	 * 
	 * @param selection
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Selection> create(@Valid @RequestBody Selection selection)
			throws Exception {
		
		return ResponseEntity.ok(selectionRepository.save(selection));
	}

	/**
	 * Read selection
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Selection> read(@PathVariable(value = "id") Long id) throws Exception {

		Optional<Selection> selection = selectionRepository.findById(id);
		if (!selection.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Selection not found for this id :: " + id);
		}

		return ResponseEntity.ok(selection.get());
	}

	/**
	 * Update selection
	 * 
	 * @param id
	 * @param selection
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Selection> update(@PathVariable(value = "id") Long id, @Valid @RequestBody Selection selection)
			throws Exception {

		if (!selection.getId().equals(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Selection id mismatch " + selection.getId() + "!=" + id);
		}
		Optional<Selection> selectionOrig = selectionRepository.findById(id);
		if (!selectionOrig.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Selection not found for this id :: " + id);
		}
		
		if(selection.getIsDefault() && !selectionOrig.get().getIsDefault()) {
			List<Selection> selections = selectionRepository.findAllByIsDefault(true);
			for(Selection s : selections) {
				s.setIsDefault(false);
				selectionRepository.save(s);
			}
		}

		return ResponseEntity.ok(selectionRepository.save(selection));
	}
	
	/**
	 * Delete selection
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity delete(@PathVariable(value = "id") Long id)
			throws Exception {
		
		Optional<Selection> selection = selectionRepository.findById(id);
		if (!selection.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Selection not found for this id :: " + id);
		}
		
		selectionRepository.deleteById(id);
		
        return ResponseEntity.ok().build();
	}
	
	/**
	 * Read all selections for select2
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/select2", method = RequestMethod.GET)
	public ResponseEntity<List<Selection>> readForSelect2() throws Exception {

		return ResponseEntity.ok(selectionRepository.findAll());
	}
	
	/**
	 * Read all agent selections
	 * 
	 * @param agentId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/agent/{agentId}", method = RequestMethod.GET)
	public ResponseEntity<List<Selection>> readAgentSelections(@PathVariable(value = "agentId") Long agentId) throws Exception {

		Optional<User> agent = userRepository.findById(agentId);
		if (!agent.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agent not found for this id :: " + agentId);
		}
		
		return ResponseEntity.ok(selectionRepository.findByAgentId(agentId));
	}

}
