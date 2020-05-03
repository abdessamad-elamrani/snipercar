package com.codevo.snipercar.service;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static final Logger logger = LoggerFactory.getLogger(Purger.class);
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Autowired
	private WebsiteRepository websiteRepository;
	
	@Autowired
	private FilterRepository filterRepository;
	
	@Autowired
	private ItemRepository itemRepository;

	@PersistenceContext
	EntityManager em;

	/**
	 * Purge stored data
	 * Keep last 100 items per each filter
	 * Keep items no older than 30 days
	 */
	public void purgeDatabase() {
		logger.info("Purger::purgeDatabase [START]");

		// purge filterItems
		int filterItemsCounter = 0;
		int itemsCounter = 0;
		List<Website> websites = websiteRepository.findAll();
		for (Website website : websites) {
			List<Filter> filters = filterRepository.findByWebsite(website);
			for (Filter filter : filters) {
				
				// Get creation date of filter 100th item
				Query selectQuery1 = em.createQuery(""
						+ " SELECT fi"
						+ " FROM FilterItem fi"
						+ " WHERE fi.filter = :filter"
						+ " ORDER BY fi.createdAt DESC, fi.updatedAt DESC"
						+ "");
				List<FilterItem> filterItems1 = selectQuery1
						.setParameter("filter", filter)
						.setFirstResult(99)
						.setMaxResults(1)
						.getResultList();
				if(filterItems1.isEmpty()) {
					continue;
				}
				Date hundredthItemDate = filterItems1.get(0).getCreatedAt();
				
				// Get 30 days past date
				Calendar thirtiethDayCalendar = Calendar.getInstance();
				thirtiethDayCalendar.add(Calendar.DATE, -30);
				Date thirtiethDayDate = thirtiethDayCalendar.getTime();
				
				// Delete filterItems
				Query selectQuery2 = em.createQuery(""
						+ " SELECT fi"
						+ " FROM FilterItem fi"
						+ " WHERE"
						+ "   fi.filter = :filter"
						+ "   AND fi.createdAt < :hundredthItemDate"
						+ "   AND fi.createdAt < :thirtiethDayDate"
						+ "");
				List<FilterItem> filterItems2 = selectQuery2
						.setParameter("filter", filter)
						.setParameter("hundredthItemDate", hundredthItemDate)
						.setParameter("thirtiethDayDate", thirtiethDayDate)
						.getResultList();
				if(filterItems2.isEmpty()) {
					continue;
				}
				Query deleteQuery = em.createQuery("DELETE FROM FilterItem fi WHERE fi IN filterItems");
				deleteQuery.setParameter("filterItems", filterItems2).executeUpdate();
//				Query query = em.createQuery(""
//						+ " DELETE FROM FilterItem fi_"
//						+ " WHERE fi_ IN ("
//						+ "   SELECT fi"
//						+ "   FROM FilterItem fi"
//						+ "   INNER JOIN ("
//						+ "     SELECT filterItem"
//						+ "     FROM FilterItem filterItem"
//						+ "     WHERE filterItem.filter = :filter"
//						+ "     ORDER BY filterItem.createdBy DESC, filterItem.updatedBy DESC"
//						+ "     LIMIT 1, OFFSET :offset"
//						+ "   ) AS lastFilterItem ON lastFilterItem.createdAt > fi.createdAt"
//						+ "   WHERE fi.filter = :filter"
//						+ " )"
//						+ "");
//				filterItemsCounter += query
//						.setParameter("filter", filter).setParameter("offset", 99)
//						.executeUpdate();
			}
		}
		
		// Delete userItems & items
		Query selectQuery = em.createQuery(""
				+ " SELECT i"
				+ " FROM Item i"
				+ " LEFT JOIN i.filterItems fi"
				+ " WHERE fi.id IS NULL"
				+ "");
		List<Item> items = selectQuery.getResultList();
		if(!items.isEmpty()) {
			itemsCounter += items.size();
			Query deleteUserItemsQuery = em.createQuery("DELETE FROM UserItem ui WHERE ui.item IN :items");
			deleteUserItemsQuery.setParameter("items", items).executeUpdate();
			Query deleteItemsQuery = em.createQuery("DELETE FROM Item i WHERE i IN :items");
			deleteItemsQuery.setParameter("items", items).executeUpdate();
		}

		logger.info("Purger::purgeDatabase [END]   filterItemsCounter=" + filterItemsCounter + ", itemsCounter=" + itemsCounter);
		return;

	}

}
