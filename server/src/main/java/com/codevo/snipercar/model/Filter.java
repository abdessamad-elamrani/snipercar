package com.codevo.snipercar.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.*;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PreUpdate;


@Entity
@Table(name = "filter")
@Getter
@Setter
@ToString 
// (exclude = { "items" })
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Filter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "website_id")
	private Website website;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "url", nullable = false)
	private String url;
	
	@Column(name = "parsed")
	private Boolean parsed = false;
	
	@JsonIgnore
	@Transient
	private Filter previousState;
	
	@JsonIgnore
	@OneToMany(mappedBy = "filter", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FilterItem> filterItems = new ArrayList<>();
	
//	@OneToMany(mappedBy = "filter")
//	private List<Item> items;

//	public Filter() {
////		this.items = new ArrayList<>();
//	}
//
//	public Filter(Website website, String name, String url) {
//		this.website = website;
//		this.name = name;
//		this.url = url;
////		this.items = new ArrayList<>();
//	}
	
	@PostLoad 
	public void setPreviousState(){
		previousState = new Filter();
		previousState.setWebsite(website);
		previousState.setName(name);
		previousState.setUrl(url);
		previousState.setParsed(parsed);
	}
	
	@PreUpdate
	public void preUpdate() {
		if (!this.previousState.getUrl().equals(this.getUrl()) ||
				!this.previousState.getWebsite().equals(this.getWebsite())) {
			this.parsed = false;
		}
	}
}
