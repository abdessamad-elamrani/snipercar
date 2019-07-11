package com.codevo.catchit.controller;

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

import com.codevo.catchit.model.Website;
import com.codevo.catchit.model.Filter;
import com.codevo.catchit.model.Field;
import com.codevo.catchit.model.Item;

import com.codevo.catchit.repository.WebsiteRepository;
import com.codevo.catchit.repository.FilterRepository;
import com.codevo.catchit.repository.FieldRepository;
import com.codevo.catchit.repository.ItemRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

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
	@PersistenceContext
    EntityManager em;
	
	@GetMapping("/items/{id}")
	@Transactional
    public List<Item> getAllItems(@PathVariable(value = "id") Long filterId) 
    		throws NotFoundException, IOException {
		
		Filter filter = filterRepository.findById(filterId)
        .orElseThrow(() -> new NotFoundException("Filter not found for this id :: " + filterId));
		
		
		
		String ref, url, title, body;
		if(filter.getWebsite().getName().toLowerCase().contains("marktplaats")) {
			
			String data = Jsoup.connect(filter.getUrl()).ignoreContentType(true).execute().body();
			JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
			
            JsonArray elts = jsonObject.getAsJsonArray("listings");
            for (int i = 0; i < elts.size(); i++) {
                JsonObject elt = elts.get(i).getAsJsonObject();
				body = elt.toString();
				ref = elt.get("itemId").getAsString();
				url = "https://www.marktplaats.nl" + elt.get("vipUrl").getAsString();
				title = elt.get("title").getAsString();
            	System.out.println("body=" + body);
				if(!ref.isEmpty() && !title.isEmpty() && !url.isEmpty() && !body.isEmpty()) {
					Item item = itemRepository.findByRef(ref);
					if (item != null) {
						item.setLastViewDate(new Date());
					} else {
						item = new Item(filter, ref, title, url, body);
					}
					em.persist(item); 
				}
            }
	        
		} else if(filter.getWebsite().getName().toLowerCase().contains("autoscout24")) {
			
			Document doc = Jsoup.connect(filter.getUrl()).get();
			
			Elements elements = doc.select("div.cl-list-element.cl-list-element-gap");
			for (Element element : elements) {
				body = element.html();
				url = element.selectFirst("a[href]").absUrl("href");
				title = element.selectFirst("h2.cldt-summary-makemodel").text();
				title += " " + element.selectFirst("h2.cldt-summary-version").text();
				ref = element.selectFirst("div.cldt-summary-full-item-main[data-articleid]").attr("data-articleid");
				if(!ref.isEmpty() && !title.isEmpty() && !url.isEmpty() && !body.isEmpty()) {
					Item item = itemRepository.findByRef(ref);
					if (item != null) {
						item.setLastViewDate(new Date());
					} else {
						item = new Item(filter, ref, title, url, body);
					}
					em.persist(item); 
				}
			}
		} else if(filter.getWebsite().getName().toLowerCase().contains("facebook")) {

		}
		
		return itemRepository.findByFilter(filter);
    }

}

