package com.codevo.snipercar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;


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

	@Column(name = "url", nullable = false)
	private String url;

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

}
