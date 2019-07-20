package com.codevo.snipercar.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.codevo.snipercar.service.*;

import javassist.NotFoundException;

import com.codevo.snipercar.model.*;
import com.codevo.snipercar.repository.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
public class Planner {
	private static final Logger logger = LoggerFactory.getLogger(Planner.class);
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	@Autowired
	private Parser parser;
	@Autowired
	private WebsiteRepository websiteRepository;
	@Autowired
	private FilterRepository filterRepository;

//	@Scheduled(fixedRate = 10000)
    public void parseAll() throws NotFoundException, IOException {
        logger.info("Planner::parseAll :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));

    	//call parser with multithread
        List<Website> websites = websiteRepository.findAll();
        for(Website website : websites) {
        	List<Filter> filters = filterRepository.findByWebsite(website);
        	//TODO: implement in multi-threads
        	for(Filter filter : filters) {
        		int counter = parser.parse(filter);
        		System.out.println("website=" + website.getName() + ", counter = " + counter);
        	}
        }
        
        return;
    }
}