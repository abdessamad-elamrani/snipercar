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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "item", uniqueConstraints={@UniqueConstraint(columnNames = {"filter_id" , "ref"})})
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "filter_id")
	private Filter filter;

	@Column(name = "ref", nullable = false)
	private String ref;
	
	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "url", nullable = false)
	private String url;

	@Lob
	@Column(name = "body", nullable = false)
	private String body;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	public Item() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}

	public Item(Filter filter, String ref, String title, String url, String body) {
		this.filter = filter;
		this.ref = ref;
		this.title = title;
		this.url = url;
		this.body = body;
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}

	public Item(Filter filter, String ref, String title, String url, String body, Date createdAt, Date updatedAt) {
		this.filter = filter;
		this.ref = ref;
		this.title = title;
		this.url = url;
		this.body = body;
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

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Item [" 
			+ "id=" + id 
			+ ", filter=" + filter 
			+ ", title=" + title 
			+ ", url=" + url 
			+ ", body=" + body 
			+ ", createdAt=" + createdAt 
			+ ", updatedAt=" + updatedAt 
		+ "]";
	}

}
