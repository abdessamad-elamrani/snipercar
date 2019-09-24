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
	private FilterRepository filterRepository;
	@Autowired
	private CompanyRepository companyRepository;

//	@Scheduled(fixedRate = 10000)
	public void callParser() throws NotFoundException, IOException {
		logger.info("Planner::callParser :: Start ", dateTimeFormatter.format(LocalDateTime.now()));
		System.out.println("Planner::callParser :: Start " + dateTimeFormatter.format(LocalDateTime.now()));

		List<CompletableFuture<Integer>> counters = new ArrayList<>();
		List<Website> websites = websiteRepository.findAll();
		for (Website website : websites) {
			counters.add(parser.parseWebsiteFilters(website));
		}
		CompletableFuture.allOf(counters.toArray(new CompletableFuture<?>[counters.size()])).join();

		logger.info("Planner::callParser :: End ", dateTimeFormatter.format(LocalDateTime.now()));
		System.out.println("Planner::callParser :: End " + dateTimeFormatter.format(LocalDateTime.now()));
		return;
	}

//	@Scheduled(fixedRate = 10000)
	public void callNotifier() throws NotFoundException, IOException {
		logger.info("Planner::callNotifier :: Start ", dateTimeFormatter.format(LocalDateTime.now()));
		System.out.println("Planner::callNotifier :: Start " + dateTimeFormatter.format(LocalDateTime.now()));

		List<CompletableFuture<Integer>> counters = new ArrayList<>();
		List<Company> companies = companyRepository.findAllActive();
		System.out.println(companies.size() + " active companies found");
		for (Company company : companies) {
			counters.add(notifier.notifyCompanyAgents(company));
		}
		CompletableFuture.allOf(counters.toArray(new CompletableFuture<?>[counters.size()])).join();

		logger.info("Planner::callNotifier :: End ", dateTimeFormatter.format(LocalDateTime.now()));
		System.out.println("Planner::callNotifier :: End " + dateTimeFormatter.format(LocalDateTime.now()));
		return;
	}

//	@Scheduled(cron = "0 0 0 * * ?")
//	@Scheduled(fixedRate = 10000)
	public void callPurger() {
		logger.info("Planner::callPurger :: Start ", dateTimeFormatter.format(LocalDateTime.now()));
		System.out.println("Planner::callPurger :: Start " + dateTimeFormatter.format(LocalDateTime.now()));

		// TODO: implement logic
		purger.purgeDatabase();

		logger.info("Planner::callPurger :: End ", dateTimeFormatter.format(LocalDateTime.now()));
		System.out.println("Planner::callPurger :: End " + dateTimeFormatter.format(LocalDateTime.now()));
		return;
	}
}