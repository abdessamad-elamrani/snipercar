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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletableFuture;

@Component
public class Planner {
	
	private static final Logger logger = LoggerFactory.getLogger(Planner.class);
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	@Autowired
	private Parser parser;
	
	@Autowired
	private Notifier notifier;
	
	@Autowired
	private Purger purger;
	
	@Autowired
	private WebsiteRepository websiteRepository;
	
	@Autowired
	private CompanyRepository companyRepository;

	/**
	 * Parser job
	 * Called each 10s
	 * 
	 * @throws NotFoundException
	 * @throws IOException
	 */
	@Scheduled(fixedRate = 10000)
	public void callParser() throws NotFoundException, IOException {
		logger.info("Planner::callParser [START]");

		List<CompletableFuture<Integer>> counters = new ArrayList<>();
		List<Website> websites = websiteRepository.findAll();
		logger.info("Planner::callParser websites=" + websites.size());
		for (Website website : websites) {
			counters.add(parser.parseWebsiteFilters(website));
		}
		//call all websites' parsers on parallel
		//wait till all parsers has finished before resuming the job
		CompletableFuture.allOf(counters.toArray(new CompletableFuture<?>[counters.size()])).join();

		logger.info("Planner::callParser [END]");
	}

	/**
	 * Notifier job
	 * Called each 10s
	 * 
	 * @throws NotFoundException
	 * @throws IOException
	 */
	@Scheduled(fixedRate = 10000)
	public void callNotifier() throws NotFoundException, IOException {
		logger.info("Planner::callNotifier [START]");

		List<CompletableFuture<Integer>> counters = new ArrayList<>();
		List<Company> companies = companyRepository.findAllActive();
		logger.info("Planner::callNotifier companies=" + companies.size());
		for (Company company : companies) {
			counters.add(notifier.notifyCompanyAgents(company));
		}
		//call all companies' notifiers on parallel
		//wait till all notifiers has finished before resuming the job
		CompletableFuture.allOf(counters.toArray(new CompletableFuture<?>[counters.size()])).join();

		logger.info("Planner::callNotifier [END]");
	}

	/**
	 * Purger job
	 * Called each midnight
	 * 
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void callPurger() {
		logger.info("Planner::callPurger [START]");

		//call purger
		purger.purgeDatabase();

		logger.info("Planner::callPurger [END]");
	}
}