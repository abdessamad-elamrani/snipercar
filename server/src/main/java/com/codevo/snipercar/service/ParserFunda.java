package com.codevo.snipercar.service;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.codevo.snipercar.component.Planner;
import com.codevo.snipercar.model.*;
import com.codevo.snipercar.repository.*;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class ParserFunda {
	
	private static final Logger logger = LoggerFactory.getLogger(ParserFunda.class);
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Autowired
	private WebsiteRepository websiteRepository;

	@Autowired
	private FilterRepository filterRepository;

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private FilterItemRepository filterItemRepository;

	@PersistenceContext
	EntityManager em;

	/**
	 * Serial parsing of all website filters
	 * 
	 * @param website
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	@Async
	public CompletableFuture<Integer> parseFundaFilters() throws NotFoundException, IOException {
		logger.info("ParserFunda::SpecialFundaParserFilters [START] ");
		int counter = 0;
		Website funda = websiteRepository.getOne((long) 9);
		List<Filter> filters = filterRepository.findByWebsite(funda);
		logger.info("ParserFunda::SpecialFundaParserFilters  , Number of filters=" + filters.size());
		for (Filter filter : filters) {
			counter += this.parseFilter(filter);
		}

		logger.info("ParserFunda::SpecialFundaParserFilters [END]  , counter=" + counter);
		return CompletableFuture.completedFuture(counter);
	}

	/**
	 * Parse a website filter
	 * 
	 * @param filter
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	public int parseFilter(Filter filter) throws NotFoundException, IOException {
		logger.info("Parser::parseFilter [START] website=" + filter.getWebsite().getName() + ", filter=" + filter.getName());
		int counter = 0;

		try {
				this.parseFunda(filter);
			
			
			if(!filter.getParsed()) {
				filter.setParsed(true);
				em.persist(filter);
			}
		} catch (Exception e) {
			logger.error("Parser::parseFilter website=" + filter.getWebsite().getName() + ", filter=" + filter.getName(), e);
		}

		logger.info("Parser::parseFilter [END] website=" + filter.getWebsite().getName() + ", filter=" + filter.getName());
		return counter;
	}
	/**
	 * Parse a Funda filter
	 * 
	 * @param filter
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	public int parseFunda(Filter filter) throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;
		// https://www.funda.nl/koop/almere/+10km/
		
		WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setUseInsecureSSL(true);
        try {

            HtmlPage page = client.getPage(filter.getUrl());
            //List<HtmlAnchor> items = page.getByXPath("//a[@data-object-url-tracking='resultlist']");
            List<HtmlElement> items = page.getByXPath("//div[@class='search-result-content']");

            if(items.isEmpty()){
                System.out.print("No items found!");
            }
            else{
                for( HtmlElement htmlElement : items){
                    url="https://www.funda.nl";
                    
                    /* getting the URL remaining part here  */
                    HtmlAnchor htmlAnchor = htmlElement.getFirstByXPath(".//a[@data-object-url-tracking='resultlist']");
                    url+=htmlAnchor.getHrefAttribute().toString();
                    
                    /* grabbing the price here, but I'm not using it .. */
                    // HtmlSpan htmlSpan = htmlElement.getFirstByXPath(".//span[@class='search-result-price']");
 
                    /* grabbing the ID here */
                    HtmlDivision htmlDivision = htmlElement.getFirstByXPath(".//div[@class='user-save-object ']");
                    ref=htmlDivision.getAttribute("data-save-object-tinyId");
                    
                    /* temporary title is url, but you should fix it. */
                    title = ""+url;  // temporary title is url, but you should fix it.
                    
                    /* adding the whole body : only relevant part html of this element */
                    body = htmlElement.asText();   //
                    
        			if (ref.length()!=0 && title.length()!=0 && url.length()>20 && body.length()!=0) {
        				Item item = itemRepository.findByRefAndWebsite(ref, filter.getWebsite());
        				FilterItem filterItem;
        				if (item != null) {
        					item.setBody(body);
        					item.setTitle(title);
        					item.setUrl(url);
        					item.setUpdatedAt(new Date());
        					filterItem = filterItemRepository.findByFilterAndItem(filter, item);
        					if(filterItem == null) {
        						filterItem = new FilterItem(filter, item);
        						item.getFilterItems().add(filterItem);
        						counter++;
        					}
        				} else {
        					item = new Item(filter.getWebsite(), ref, title, url, body);
        					filterItem = new FilterItem(filter, item);
        					item.getFilterItems().add(filterItem);
        					counter++;
        				}
        				em.persist(item);
        			}
                    
                    

                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        } 
		return counter;
		
	}
	
	/**
	 * Parse a Marktplaats filter
	 * 
	 * @param filter
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	public int parseMarktplaats(Filter filter) throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;

		// https://www.marktplaats.nl/l/auto-s/bmw/#searchInTitleAndDescription:false
		// https://www.marktplaats.nl/lrp/api/search?l1CategoryId=91&l2CategoryId=96&limit=30&offset=0&viewOptions=list-view

		String data = Jsoup.connect(filter.getUrl()).ignoreContentType(true).execute().body();
		JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);

		JsonArray elts = jsonObject.getAsJsonArray("listings");
		for (int i = 0; i < elts.size(); i++) {
			JsonObject elt = elts.get(i).getAsJsonObject();
			body = elt.toString();
			ref = elt.get("itemId").getAsString();
			url = "https://www.marktplaats.nl" + elt.get("vipUrl").getAsString();
			title = elt.get("title").getAsString();
			if (!ref.isEmpty() && !title.isEmpty() && !url.isEmpty() && !body.isEmpty()) {
				Item item = itemRepository.findByRefAndWebsite(ref, filter.getWebsite());
				FilterItem filterItem;
				if (item != null) {
					item.setBody(body);
					item.setTitle(title);
					item.setUrl(url);
					item.setUpdatedAt(new Date());
					filterItem = filterItemRepository.findByFilterAndItem(filter, item);
					if(filterItem == null) {
						filterItem = new FilterItem(filter, item);
						item.getFilterItems().add(filterItem);
						counter++;
					}
				} else {
					item = new Item(filter.getWebsite(), ref, title, url, body);
					filterItem = new FilterItem(filter, item);
					item.getFilterItems().add(filterItem);
					counter++;
				}
				em.persist(item);
			}
		}

		return counter;

	}

	/**
	 * Parse an AutoScout24 filter
	 * 
	 * @param filter
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
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
			if (!ref.isEmpty() && !title.isEmpty() && !url.isEmpty() && !body.isEmpty()) {
				Item item = itemRepository.findByRefAndWebsite(ref, filter.getWebsite());
				FilterItem filterItem;
				if (item != null) {
					item.setBody(body);
					item.setTitle(title);
					item.setUrl(url);
					item.setUpdatedAt(new Date());
					filterItem = filterItemRepository.findByFilterAndItem(filter, item);
					if(filterItem == null) {
						filterItem = new FilterItem(filter, item);
						item.getFilterItems().add(filterItem);
						counter++;
					}
				} else {
					item = new Item(filter.getWebsite(), ref, title, url, body);
					filterItem = new FilterItem(filter, item);
					item.getFilterItems().add(filterItem);
					counter++;
				}
				em.persist(item);
			}
		}

		return counter;
	}

	/**
	 * Parse a Facebook filter
	 * 
	 * @param filter
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	public int parseFacebook(Filter filter) throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;

		// do nothing

		return counter;
	}

	/**
	 * Parse an Anwb filter
	 * 
	 * @param filter
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	private int parseAnwb(Filter filter) throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;

		// https://www.anwb.nl/auto/kopen/zoeken/merk=bmw
		// https://api.anwb.nl/occasion-hexon-search?vehicle.brand.keyword=bmw&limit=24&viewwrapper=grid

		// Headers : x-anwb-client-id = innzlrw2VjJNTsfWGaTK0C887VOO5mIJ
		String data = Jsoup.connect(filter.getUrl()).header("x-anwb-client-id", "innzlrw2VjJNTsfWGaTK0C887VOO5mIJ")
				.ignoreContentType(true).execute().body();
		JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);

		JsonArray elts = jsonObject.getAsJsonArray("results");
		JsonObject vehicle;
		for (int i = 0; i < elts.size(); i++) {
			JsonObject elt = elts.get(i).getAsJsonObject();
			body = elt.toString();
			ref = elt.get("id").getAsString();
			vehicle = elt.get("vehicle").getAsJsonObject();
			url = "https://www.anwb.nl/auto/kopen/detail/merk=" + this.slugify(vehicle.get("brand").getAsString())
					+ "/model=" + this.slugify(vehicle.get("model").getAsString()) + "/overzicht/"
					+ elt.get("id").getAsString();
			title = elt.get("advertisement").getAsJsonObject().get("title").getAsString();
			if (!ref.isEmpty() && !title.isEmpty() && !url.isEmpty() && !body.isEmpty()) {
				Item item = itemRepository.findByRefAndWebsite(ref, filter.getWebsite());
				FilterItem filterItem;
				if (item != null) {
					item.setBody(body);
					item.setTitle(title);
					item.setUrl(url);
					item.setUpdatedAt(new Date());
					filterItem = filterItemRepository.findByFilterAndItem(filter, item);
					if(filterItem == null) {
						filterItem = new FilterItem(filter, item);
						item.getFilterItems().add(filterItem);
						counter++;
					}
				} else {
					item = new Item(filter.getWebsite(), ref, title, url, body);
					filterItem = new FilterItem(filter, item);
					item.getFilterItems().add(filterItem);
					counter++;
				}
				em.persist(item);
			}
		}

		return counter;
	}

	/**
	 * Parse a Gaspedaal filter
	 * 
	 * @param filter
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	private int parseGaspedaal(Filter filter) throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;

		Document doc = Jsoup.connect(filter.getUrl()).get();

		Elements elements = doc.select("li.occasion.popup_click_event.aec_popup_click");
		for (Element element : elements) {
			body = element.html();
			url = element.selectFirst("a[href]").absUrl("href");
			title = element.selectFirst("div.occ_cartitle.popup_title").text();
			ref = element.selectFirst("a[href]").attr("data-id");
			if (!ref.isEmpty() && !title.isEmpty() && !url.isEmpty() && !body.isEmpty()) {
				Item item = itemRepository.findByRefAndWebsite(ref, filter.getWebsite());
				FilterItem filterItem;
				if (item != null) {
					item.setBody(body);
					item.setTitle(title);
					item.setUrl(url);
					item.setUpdatedAt(new Date());
					filterItem = filterItemRepository.findByFilterAndItem(filter, item);
					if(filterItem == null) {
						filterItem = new FilterItem(filter, item);
						item.getFilterItems().add(filterItem);
						counter++;
					}
				} else {
					item = new Item(filter.getWebsite(), ref, title, url, body);
					filterItem = new FilterItem(filter, item);
					item.getFilterItems().add(filterItem);
					counter++;
				}
				em.persist(item);
			}
		}

		return counter;
	}

	/**
	 * Parse an Autoweek filter
	 * 
	 * @param filter
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	private int parseAutoweek(Filter filter) throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;

		// www.autoweek.nl/occasions/bmw
		// https://public-api.autoweek.nl/v1/occasions/?filters_new=true&sort=insertion_age&page=1&make_models_new=bmw&format=json&cachebuster=1563308015&ascending=1

		String data = Jsoup.connect(filter.getUrl()).ignoreContentType(true).execute().body();
		JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);

		JsonArray elts = jsonObject.getAsJsonArray("occasions");
		for (int i = 0; i < elts.size(); i++) {
			JsonObject elt = elts.get(i).getAsJsonObject();
			if (!elt.has("mileage")) {
				continue;
			}
			body = elt.toString();
			ref = elt.get("url").getAsString();
			Pattern pattern = Pattern.compile("/(\\d+)/");
			Matcher matcher = pattern.matcher(ref);
			if (matcher.find()) {
				ref = matcher.group(1);
			}
			url = "https://www.autoweek.nl" + elt.get("url").getAsString();
			title = elt.get("name").getAsString();
			if (!ref.isEmpty() && !title.isEmpty() && !url.isEmpty() && !body.isEmpty()) {
				Item item = itemRepository.findByRefAndWebsite(ref, filter.getWebsite());
				FilterItem filterItem;
				if (item != null) {
					item.setBody(body);
					item.setTitle(title);
					item.setUrl(url);
					item.setUpdatedAt(new Date());
					filterItem = filterItemRepository.findByFilterAndItem(filter, item);
					if(filterItem == null) {
						filterItem = new FilterItem(filter, item);
						item.getFilterItems().add(filterItem);
						counter++;
					}
				} else {
					item = new Item(filter.getWebsite(), ref, title, url, body);
					filterItem = new FilterItem(filter, item);
					item.getFilterItems().add(filterItem);
					counter++;
				}
				em.persist(item);
			}
		}

		return counter;
	}

	/**
	 * Slugify string
	 * 
	 * @param input
	 * @return
	 */
	public String slugify(String input) {
		Pattern NONLATIN = Pattern.compile("[^\\w-]");
		Pattern WHITESPACE = Pattern.compile("[\\s]");

		String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
		String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
		String slug = NONLATIN.matcher(normalized).replaceAll("");
		return slug.toLowerCase(Locale.ENGLISH);
	}

	/**
	 * Parse an Autotrader filter
	 * 
	 * @param filter
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	private int parseAutotrader(Filter filter) throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;

		// https://www.autotrader.nl/auto/bmw/
		// https://www.autotrader.nl/api/v2/search-listings?atype=C&sort=standard&desc=0&fregfrom=1994&safemake=bmw

		String data = Jsoup.connect(filter.getUrl()).ignoreContentType(true).execute().body();
		JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);

		JsonArray elts = jsonObject.getAsJsonArray("listings");
		for (int i = 0; i < elts.size(); i++) {
			JsonObject elt = elts.get(i).getAsJsonObject();
			body = elt.toString();
			ref = elt.get("articleId").getAsString();
			url = "https://www.autotrader.nl/auto/voertuig/"
					+ this.slugify(elt.get("title").getAsString() + " " + elt.get("fuel").getAsString()) + "/"
					+ elt.get("id").getAsString();
			title = elt.get("title").getAsString();
			if (!ref.isEmpty() && !title.isEmpty() && !url.isEmpty() && !body.isEmpty()) {
				Item item = itemRepository.findByRefAndWebsite(ref, filter.getWebsite());
				FilterItem filterItem;
				if (item != null) {
					item.setBody(body);
					item.setTitle(title);
					item.setUrl(url);
					item.setUpdatedAt(new Date());
					filterItem = filterItemRepository.findByFilterAndItem(filter, item);
					if(filterItem == null) {
						filterItem = new FilterItem(filter, item);
						item.getFilterItems().add(filterItem);
						counter++;
					}
				} else {
					item = new Item(filter.getWebsite(), ref, title, url, body);
					filterItem = new FilterItem(filter, item);
					item.getFilterItems().add(filterItem);
					counter++;
				}
				em.persist(item);
			}
		}

		return counter;
	}

	/**
	 * Parse a Viabovag filter
	 * 
	 * @param filter
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	private int parseViabovag(Filter filter) throws NotFoundException, IOException {
		String ref, url, title, body;
		int counter = 0;

		Document doc = Jsoup.connect(filter.getUrl()).get();

		Elements elements = doc.select("div.search-result");
		for (Element element : elements) {
			body = element.html();
			url = element.selectFirst("a.search-result__vehicle-section[href]").absUrl("href");
			title = element.selectFirst(".search-result__vehicle-title[title]").attr("title");
			ref = element.selectFirst("a.search-result__vehicle-section[href]").absUrl("href");
			Pattern pattern = Pattern.compile("/auto/([^/]+)");
			Matcher matcher = pattern.matcher(ref);
			if (matcher.find()) {
				ref = matcher.group(1);
			}
			if (!ref.isEmpty() && !title.isEmpty() && !url.isEmpty() && !body.isEmpty()) {
				Item item = itemRepository.findByRefAndWebsite(ref, filter.getWebsite());
				FilterItem filterItem;
				if (item != null) {
					item.setBody(body);
					item.setTitle(title);
					item.setUrl(url);
					item.setUpdatedAt(new Date());
					filterItem = filterItemRepository.findByFilterAndItem(filter, item);
					if(filterItem == null) {
						filterItem = new FilterItem(filter, item);
						item.getFilterItems().add(filterItem);
						counter++;
					}
				} else {
					item = new Item(filter.getWebsite(), ref, title, url, body);
					filterItem = new FilterItem(filter, item);
					item.getFilterItems().add(filterItem);
					counter++;
				}
				em.persist(item);
			}
		}

		return counter;
	}

}