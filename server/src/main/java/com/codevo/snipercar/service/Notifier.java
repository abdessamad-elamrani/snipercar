package com.codevo.snipercar.service;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
import org.springframework.scheduling.annotation.Async;

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
public class Notifier {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ItemRepository itemRepository;

	@PersistenceContext
	EntityManager em;

	@Async
	public CompletableFuture<Integer> notifyCompanyAgents(Company company) throws NotFoundException, IOException {
		int counter = 0;

		List<User> agents = userRepository.findAllActiveAgentsByCompany(company);
		System.out.println(agents.size() + " active agents found for company " + company.getName());
		for(User agent : agents) {
			System.out.println("agent " + agent.getUsername());
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -agent.getCompany().getSla().getLatency());
			Date createdAt = cal.getTime();
			List<Item> items = itemRepository.findAgentPendingItems(agent, createdAt);
			System.out.println(items.size() + " items to be sent to agent " + agent.getUsername());
			for(Item item : items) {
				UserItem userItem = new UserItem(agent, item);
				if(agent.getSmsNotif()) {
					userItem.setSmsSent(this.sendSms(agent, item));
				}
				if(agent.getEmailNotif()) {
					userItem.setEmailSent(this.sendEmail(agent, item));
				}
				em.persist(userItem);
			}
		}
		
		return CompletableFuture.completedFuture(counter);
	}
	
	private boolean sendSms(User agent, Item item) {
		
		//TODO: to be implemented
		
		return true;
	}
	
	private boolean sendEmail(User agent, Item item) {
		
		//TODO: to be implemented
		
		return true;
	}

}
