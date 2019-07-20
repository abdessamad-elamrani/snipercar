package com.codevo.snipercar.model;

import java.util.Date;
import java.util.List;

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

import lombok.*;

@Entity
@Table(name = "item", uniqueConstraints = { @UniqueConstraint(columnNames = { "filter_id", "ref" }) })
@Getter
@Setter
@ToString
@EqualsAndHashCode
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

}
