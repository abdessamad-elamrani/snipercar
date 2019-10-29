package com.codevo.snipercar.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.*;

import com.fasterxml.jackson.annotation.*;

@Entity
@Table(name = "item", uniqueConstraints = { @UniqueConstraint(columnNames = { "website_id", "ref" }) })
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "website_id")
	private Website website;

	@Column(name = "ref", nullable = false)
	private String ref;

	@Column(name = "title", nullable = false)
	private String title;

	@Lob
	@Column(name = "url", nullable = false)
	private String url;

	@Lob
	@Column(name = "body", nullable = false)
	private String body;
	
	@JsonIgnore
	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FilterItem> filterItems = new ArrayList<>();

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt = new Date();

	@Column(name = "updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt = new Date();

//	public Item() {
//		this.createdAt = new Date();
//		this.updatedAt = new Date();
//	}
//
	public Item(Website website, String ref, String title, String url, String body) {
		this.website = website;
		this.ref = ref;
		this.title = title;
		this.url = url;
		this.body = body;
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}

}
