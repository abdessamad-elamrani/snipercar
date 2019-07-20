package com.codevo.catchit.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.*;

@Entity
@Table(name = "filter")
@Getter
@Setter
@ToString // (exclude = { "items" })
@EqualsAndHashCode
public class Filter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "website_id")
	private Website website;

	@Column(name = "url", nullable = false)
	private String url;

//	@OneToMany(mappedBy = "filter")
//	private List<Item> items;

	public Filter() {
//		this.items = new ArrayList<>();
	}

	public Filter(Website website, String url) {
		this.website = website;
		this.url = url;
//		this.items = new ArrayList<>();
	}

}
