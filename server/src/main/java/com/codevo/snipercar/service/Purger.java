package com.codevo.snipercar.service;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

import com.codevo.snipercar.model.*;
import com.codevo.snipercar.repository.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class Purger {

	@Autowired
	private WebsiteRepository websiteRepository;
	
	@Autowired
	private FilterRepository filterRepository;
	
	@Autowired
	private ItemRepository itemRepository;

	@PersistenceContext
	EntityManager em;

	public int purgeDatabase() {

		// purge filterItems
		int filterItemsCounter = 0;
		List<Website> websites = websiteRepository.findAll();
		for (Website website : websites) {
			List<Filter> filters = filterRepository.findByWebsite(website);
			for (Filter filter : filters) {
				Query query = em.createQuery(""
						+ " DELETE fi"
						+ " FROM FilterItem fi"
						+ " INNER JOIN ("
						+ "   SELECT filterItem"
						+ "   FROM Item filterItem"
						+ "   WHERE filterItem.filter = :filter"
						+ "   ORDER BY filterItem.createdBy DESC, filterItem.updatedBy DESC"
						+ "   LIMIT 1, OFFSET :offset"
						+ " ) AS lastFilterItem ON lastFilterItem.createdAt > fi.createdAt"
						+ " WHERE fi.filter = :filter"
						+ "");
				filterItemsCounter += query.setParameter("filter", filter)
						.setParameter("offset", 99).executeUpdate();
			}
		}
		//purge orphan items
		Query query = em.createQuery(""
				+ " DELETE i"
				+ " FROM Item i"
				+ " LEFT JOIN i.filterItem fi"
				+ " WHERE fi.id IS NULL"
				+ "");
		int itemsCounter = query.executeUpdate();

		return filterItemsCounter;

	}

}
