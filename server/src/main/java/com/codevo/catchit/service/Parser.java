package com.codevo.catchit.service;

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

import com.codevo.catchit.model.*;
import com.codevo.catchit.repository.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class Parser {
	@Autowired
	private ItemRepository itemRepository;
	@PersistenceContext
    EntityManager em;
	
	public int parse(Filter filter) throws NotFoundException, IOException {
		int counter = 0;
		
		if(filter.getWebsite().getName().toLowerCase().contains("marktplaats")) {
			counter = this.parseMarktplaats(filter);
		} else if(filter.getWebsite().getName().toLowerCase().contains("autoscout24")) {
			counter = this.parseAutoScout24(filter);
		} else if(filter.getWebsite().getName().toLowerCase().contains("facebook")) {
			counter = this.parseFacebook(filter);
		} else if(filter.getWebsite().getName().toLowerCase().contains("anwb")) {
			counter = this.parseAnwb(filter);
		} else if(filter.getWebsite().getName().toLowerCase().contains("gaspedaal")) {
			counter = this.parseGaspedaal(filter);
		} else if(filter.getWebsite().getName().toLowerCase().contains("autoweek")) {
			counter = this.parseAutoweek(filter);
		} else if(filter.getWebsite().getName().toLowerCase().contains("autotrader")) {
			counter = this.parseAutotrader(filter);
		} else if(filter.getWebsite().getName().toLowerCase().contains("viabovag")) {
			counter = this.parseViabovag(filter);
		}
		
		return counter;
	}

	public int parseMarktplaats(Filter filter) throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;
		
		String data = Jsoup.connect(filter.getUrl()).ignoreContentType(true).execute().body();
		JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
		
        JsonArray elts = jsonObject.getAsJsonArray("listings");
        for (int i = 0; i < elts.size(); i++) {
            JsonObject elt = elts.get(i).getAsJsonObject();
			body = elt.toString();
			ref = elt.get("itemId").getAsString();
			url = "https://www.marktplaats.nl" + elt.get("vipUrl").getAsString();
			title = elt.get("title").getAsString();
//        	System.out.println("body=" + body);
			if(!ref.isEmpty() && !title.isEmpty() && !url.isEmpty() && !body.isEmpty()) {
				Item item = itemRepository.findByRef(ref);
				if (item != null) {
					item.setUpdatedAt(new Date());
				} else {
					item = new Item(filter, ref, title, url, body);
					counter++;
				}
				em.persist(item); 
			}
        }
        
        return counter;
		
	}

	public int parseAutoScout24(Filter filter) throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;
		
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
					item.setUpdatedAt(new Date());
				} else {
					item = new Item(filter, ref, title, url, body); 
					counter++;
				}
				em.persist(item);
			}
		}
		
        
        return counter;
	}
	
	public int parseFacebook(Filter filter) throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;
		
		//do nothing
        
        return counter;
	}

	private int parseAnwb(Filter filter)  throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;

		// reactJS
		// website	= www.anwb.nl
		// filter	= https://www.anwb.nl/auto/kopen/zoeken/merk=bmw
		// filter1	= https://api.anwb.nl/occasion-hexon-search?vehicle.brand.keyword=bmw&limit=24&viewwrapper=grid
		// body 	= results[i]
		// title 	= results[i].advertisement.title
		// ref 		= results[i].id
		// url 		= https://www.anwb.nl/auto/kopen/detail/merk=bmw/model=z4/overzicht/530893222?/merk=bmw
        
        return counter;
	}

	private int parseGaspedaal(Filter filter)  throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;
		
		Document doc = Jsoup.connect(filter.getUrl()).get();
		
		Elements elements = doc.select("li.occasion.popup_click_event.aec_popup_click");
		for (Element element : elements) {
			body = element.html();
			url = element.selectFirst("a[href]").absUrl("href");
			title = element.selectFirst("div.occ_cartitle.popup_title").text();
			ref = element.selectFirst("a[href]").attr("data-id");
			if(!ref.isEmpty() && !title.isEmpty() && !url.isEmpty() && !body.isEmpty()) {
				Item item = itemRepository.findByRef(ref);
				if (item != null) {
					item.setUpdatedAt(new Date());
				} else {
					item = new Item(filter, ref, title, url, body); 
					counter++;
				}
				em.persist(item);
			}
		}
        
        return counter;
	}
	
	private int parseAutoweek(Filter filter)  throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;

//		www.autoweek.nl/occasions/
        
        return counter;
	}

	private int parseAutotrader(Filter filter)  throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;

//		www.autotrader.nl/
        
        return counter;
	}
	
	private int parseViabovag(Filter filter)  throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;

//		www.viabovag.nl
        
        return counter;
	}

}
