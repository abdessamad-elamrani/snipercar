package com.codevo.snipercar.controller;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
//import javax.mail.internet.*;

import javassist.NotFoundException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.codevo.snipercar.model.*;
import com.codevo.snipercar.repository.*;
import com.codevo.snipercar.component.*;
import com.codevo.snipercar.service.*;

import java.io.ByteArrayOutputStream;
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
@RequestMapping("/api")
public class ItemController {

	@Autowired
	private Parser parser;
	@Autowired
	private FilterRepository filterRepository;
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
    public JavaMailSender mailSender;
	
//	@Autowired
//	public Gmail gmailService;


	@GetMapping("/items/{id}")
	@Transactional
	public List<Item> getAllItems(@PathVariable(value = "id") Long filterId) throws NotFoundException, IOException {

		Filter filter = filterRepository.findById(filterId)
				.orElseThrow(() -> new NotFoundException("Filter not found for this id :: " + filterId));

		int counter = parser.parseFilter(filter);
		System.out.println("website=" + filter.getWebsite().getName() + ", counter = " + counter);

		return itemRepository.findByFilter(filter);
	}

	@PreAuthorize("hasRole('USER')")
	// @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(value = "/authorities", method = RequestMethod.GET)
	public ResponseEntity<?> refreshToken(Authentication authentication) throws Exception {

		JsonObject content = new JsonObject();
		content.addProperty("roles", authentication.getAuthorities().toString());

		return ResponseEntity.ok(content);
	}

	@RequestMapping(value = "/send/sms", method = RequestMethod.POST)
	public ResponseEntity<?> sendSms() throws Exception {
//
		Map<String, String> request = new HashMap<>();
		request.put("api_key", "aa5a42af");
		request.put("api_secret", "aIjLiSmN3ReN7r9y");
		request.put("from", "SNIPERCAR");
		request.put("to", "212658529531");
		String text = "";
		text += "Hello,\n" ;
		text += "\n";
		text += "New advert identified.\n";
		text += "http://www.codevo-consulting.com:8081/items/1" + "\n";
		text += "\n";
		text += "SniperCar Team";
		request.put("text", text);
		String data = Jsoup.connect("https://rest.nexmo.com/sms/json")
				.ignoreContentType(true)
				.userAgent("Mozilla/5.0")
				.data(request)
				.method(Method.POST)
				.execute()
				.body();
		JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
		
		boolean isSent = false;
		try {
			isSent = !jsonObject.isJsonNull() && 
					jsonObject.has("messages") &&
					jsonObject.getAsJsonArray("messages").size() > 0 &&
					jsonObject.getAsJsonArray("messages").get(0).getAsJsonObject().has("status") &&
					jsonObject.getAsJsonArray("messages").get(0).getAsJsonObject().get("status").getAsInt() == 0
					;
		} catch(Exception e) {
			//Do nothing
		}

		Map<String, Object> content = new HashMap<>();
		content.put("isSent", isSent);

		return ResponseEntity.ok(content);
		
	}

	@RequestMapping(value = "/send/email", method = RequestMethod.POST)
	public ResponseEntity<?> sendEmail() throws Exception, MailException {
		
//		Login: noreply@snipercar.nl
//		Password: 7<hKGUWM2
		
//		Properties props = new Properties();
//        Session session = Session.getDefaultInstance(props, null);
//
//        MimeMessage emailContent = new MimeMessage(session);
//
//        emailContent.setFrom(new InternetAddress("noreply@snipercar.nl"));
//        emailContent.addRecipient(javax.mail.Message.RecipientType.TO,
//                new InternetAddress("bouali.brahim@gmail.com"));
//        emailContent.setSubject("test from SniperCar App");
//        emailContent.setText("test from SniperCar App");
//        
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        emailContent.writeTo(buffer);
//        byte[] bytes = buffer.toByteArray();
//        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
//        Message message;// = new Message();
//        message.setRaw(encodedEmail);
//        
//        message = service.users().messages().send(userId, message).execute();
//
//        System.out.println("Message id: " + message.getId());
//        System.out.println(message.toPrettyString());
		
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("bouali.brahim@gmail.com"); 
        message.setSubject("test from SniperCar App");
        String text = "";
		text += "Hello,\n" ;
		text += "\n";
		text += "New advert identified.\n";
		text += "http://www.codevo-consulting.com:8081/items/1" + "\n";
		text += "\n";
		text += "SniperCar Team";
        message.setText(text);
        
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
////        mailSender.setUsername("noreply@snipercar.nl");
////        mailSender.setPassword("7<hKGUWM2");
//        mailSender.setUsername("bouali.brahim@gmail.com");
//        mailSender.setPassword("jnaxrok4");
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
        
        mailSender.send(message);

		Map<String, Object> content = new HashMap<>();
		content.put("status", "send Email response");

		return ResponseEntity.ok(content);
	}
	
	@RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
	public ResponseEntity<Item> read(@PathVariable(value = "id") Long id) throws Exception {

		Optional<Item> item = itemRepository.findById(id);
		if (!item.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found for this id :: " + id);
		}
		
		return ResponseEntity.ok(item.get());
	}

}
