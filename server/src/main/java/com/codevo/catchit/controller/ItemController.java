package com.codevo.catchit.controller;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codevo.catchit.model.Website;
import com.codevo.catchit.model.Filter;
import com.codevo.catchit.model.Field;
import com.codevo.catchit.model.Item;

import com.codevo.catchit.repository.WebsiteRepository;

import com.codevo.catchit.repository.FilterRepository;
import com.codevo.catchit.repository.FieldRepository;
import com.codevo.catchit.repository.ItemRepository;


//import com.codevo.catchit.exception.ResourceNotFoundException;
//import com.codevo.catchit.model.Employee;
//import com.codevo.catchit.repository.EmployeeRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController 
@RequestMapping("/api/v1")
public class ItemController {
	
	@Autowired
	private WebsiteRepository websiteRepository;
	@Autowired
	private FilterRepository filterRepository;
	@Autowired
	private FieldRepository fieldRepository;
	@Autowired
	private ItemRepository itemRepository;
	
	
	@GetMapping("/items/{id}")
    public List<Item> getAllItems(@PathVariable(value = "id") Long filterId) 
    		throws NotFoundException {

		Filter filter = filterRepository.findById(filterId)
        .orElseThrow(() -> new NotFoundException("Filter not found for this id :: " + filterId));
		
		return itemRepository.findByFilter(filter);
    }

}
