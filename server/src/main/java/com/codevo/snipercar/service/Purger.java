package com.codevo.snipercar.service;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
				Query selectQuery2 = em.createQuery(""
						+ " SELECT fi"
						+ " FROM FilterItem fi"
						+ " WHERE"
						+ "   fi.filter = :filter"
						+ "   AND fi.createdAt < :createdAt"
						+ "");
				List<FilterItem> filterItems2 = selectQuery2
						.setParameter("filter", filter)
						.setParameter("createdAt", filterItems1.get(0).getCreatedAt())
						.getResultList();
				if(filterItems2.isEmpty()) {
					continue;
				}
				List<Long> ids = new ArrayList<Long>();
				for(FilterItem filterItem : filterItems2) {
					ids.add(filterItem.getId());
				}
				Query deleteQuery = em.createQuery("DELETE FROM FilterItem fi WHERE fi.id IN :ids");
				deleteQuery.setParameter("ids", ids).executeUpdate();
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
		//purge orphan items
		Query selectQuery = em.createQuery(""
				+ " SELECT i"
				+ " FROM Item i"
				+ " LEFT JOIN i.filterItems fi"
				+ " WHERE fi.id IS NULL"
				+ "");
		List<Item> items = selectQuery.getResultList();
		if(!items.isEmpty()) {
			List<Long> ids = new ArrayList<Long>();
			for(Item item : items) {
				ids.add(item.getId());
			}
			Query deleteQuery = em.createQuery("DELETE FROM Item i WHERE i.id IN :ids");
			deleteQuery.setParameter("ids", ids).executeUpdate();
		}

		return filterItemsCounter;

	}

}
