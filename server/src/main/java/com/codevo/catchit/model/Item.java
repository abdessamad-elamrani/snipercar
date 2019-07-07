package com.codevo.catchit.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "item")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "filter_id")
	private Filter filter;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "url", nullable = false)
	private String url;

	@Lob
	@Column(name = "html", nullable = false)
	private String html;

	@Column(name = "first_view_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date firstViewDate;

	@Column(name = "last_view_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastViewDate;

	public Item() {
		this.firstViewDate = new Date();
		this.lastViewDate = new Date();
	}

	public Item(Filter filter, String title, String url, String html) {
		this.filter = filter;
		this.title = title;
		this.url = url;
		this.html = html;
		this.firstViewDate = new Date();
		this.lastViewDate = new Date();
	}

	public Item(Filter filter, String title, String url, String html, Date firstViewDate, Date lastViewDate) {
		this.filter = filter;
		this.title = title;
		this.url = url;
		this.html = html;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public Date getFirstViewDate() {
		return firstViewDate;
	}

	public void setFirstViewDate(Date firstViewDate) {
		this.firstViewDate = firstViewDate;
	}

	public Date getLastViewDate() {
		return lastViewDate;
	}

	public void setLastViewDate(Date lastViewDate) {
		this.lastViewDate = lastViewDate;
	}

	@Override
	public String toString() {
		return "Item [" 
			+ "id=" + id 
			+ ", filter=" + filter 
			+ ", title=" + title 
			+ ", url=" + url 
			+ ", html=" + html 
			+ ", firstViewDate=" + firstViewDate 
			+ ", lastViewDate=" + lastViewDate 
		+ "]";
	}

}
