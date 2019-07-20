package com.codevo.snipercar.controller;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
@RequestMapping("/api/v1")
public class ItemController {
	
	@Autowired
	private Parser parser;
	@Autowired
	private FilterRepository filterRepository;
	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping("/items/{id}")
	@Transactional
    public List<Item> getAllItems(@PathVariable(value = "id") Long filterId) 
    		throws NotFoundException, IOException {
		
		Filter filter = filterRepository.findById(filterId)
        .orElseThrow(() -> new NotFoundException("Filter not found for this id :: " + filterId));
		
		int counter = parser.parse(filter);
		System.out.println("website=" + filter.getWebsite().getName() + ", counter = " + counter);
		
		return itemRepository.findByFilter(filter);
    }

}

