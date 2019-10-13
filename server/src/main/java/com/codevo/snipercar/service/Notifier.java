package com.codevo.snipercar.service;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
import org.jsoup.Connection.Method;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
	
	private static final Logger logger = LoggerFactory.getLogger(Notifier.class);
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
    public JavaMailSender mailSender;

	@PersistenceContext
	EntityManager em;

	@Async
	public CompletableFuture<Integer> notifyCompanyAgents(Company company) throws NotFoundException, IOException {
		logger.info("Notifier::notifyCompanyAgents [START] company=" + company.getName());
		int counter = 0;

		List<User> agents = userRepository.findAllActiveAgentsByCompany(company);
		logger.info("Notifier::notifyCompanyAgents company=" + company.getName() + ", agents=" + agents.size());
		for (User agent : agents) {
			try {
				Calendar latencyCalendar = Calendar.getInstance();
				latencyCalendar.add(Calendar.MINUTE, -agent.getCompany().getSla().getLatency());
				Date latencyDate = latencyCalendar.getTime();
				List<Item> items = itemRepository.findAgentPendingItems(agent, latencyDate);
				for (Item item : items) {
					UserItem userItem = new UserItem(agent, item);
					if (agent.getSmsNotif()) {
						this.sendSms(userItem);
					}
					if (agent.getEmailNotif()) {
						this.sendEmail(userItem);
					}
					em.persist(userItem);
				}
			} catch (Exception e) {
				logger.error("Notifier::notifyCompanyAgents company=" + company.getName() + ", agent=" + agent.getName(), e);
			}
		}

		logger.info("Notifier::notifyCompanyAgents [END] company=" + company.getName() + ", counter=" + counter);
		return CompletableFuture.completedFuture(counter);
	}

	private void sendSms(UserItem userItem) throws IOException {
		
		User agent = userItem.getUser();
		Item item = userItem.getItem();
		
		boolean smsSent = false;
		String smsLog = "";
		
		label: try {
			// Validate phone number format
			String phone = agent.getPhone().replaceAll("[^\\d]|^0+", "");
			if(phone.isEmpty()) {
				smsSent = false;
				smsLog = "Invalid Phone number";
				break label;
			}
			
			// Send SMS
			Map<String, String> request = new HashMap<>();
			request.put("api_key", "aa5a42af");
			request.put("api_secret", "aIjLiSmN3ReN7r9y");
			request.put("from", "SNIPERCAR");
			request.put("to", phone);
			String text = "";
			text += "Hello,\n";
			text += "\n";
			text += "New advert identified.\n";
			text += "www.codevo-consulting.com:8081/web/item/" + item.getId() + "\n";
			text += "\n";
			text += "SniperCar Team";
			request.put("text", text);
			String data = Jsoup.connect("https://rest.nexmo.com/sms/json").ignoreContentType(true)
					.userAgent("Mozilla/5.0").data(request).method(Method.POST).execute().body();
			JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);

			smsSent = !jsonObject.isJsonNull() && jsonObject.has("messages")
					&& jsonObject.getAsJsonArray("messages").size() > 0
					&& jsonObject.getAsJsonArray("messages").get(0).getAsJsonObject().has("status")
					&& jsonObject.getAsJsonArray("messages").get(0).getAsJsonObject().get("status").getAsInt() == 0;
			smsLog = data;
		} catch (Exception e) {
			logger.error("Notifier::sendSms company=" + agent.getCompany().getName() + ", agent=" + agent.getName(), ", phone=" + agent.getPhone(), e);
			smsLog = e.getMessage();
		}

		userItem.setSmsSent(smsSent);
		userItem.setSmsLog(smsLog);
	}

	private void sendEmail(UserItem userItem) {
		
		User agent = userItem.getUser();
		Item item = userItem.getItem();
		
		boolean emailSent = false;
		String emailLog = "";
		
		label: try {
			// Validate Email Address
	        String email = agent.getEmail().replaceAll(" ", "");
			if(email.isEmpty()) {
				emailSent = false;
				emailLog = "Invalid Email Address";
				break label;
			}
			
			// Send Email
			SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(email); 
	        message.setSubject("[SNIPERCAR] New advert identified");
			String text = "";
			text += "Hello,\n";
			text += "\n";
			text += "New advert identified.\n";
			text += "www.codevo-consulting.com:8081/web/item/" + item.getId() + "\n";
			text += "\n";
			text += "SniperCar Team"; 
	        message.setText(text);
	        mailSender.send(message);
	        emailSent = true;
		} catch (Exception e) {
			logger.error("Notifier::sendEmail company=" + agent.getCompany().getName() + ", agent=" + agent.getName(), ", email=" + agent.getEmail(), e);
			emailLog = e.getMessage();
		}
		
		userItem.setEmailSent(emailSent);
		userItem.setEmailLog(emailLog);
	}

}
