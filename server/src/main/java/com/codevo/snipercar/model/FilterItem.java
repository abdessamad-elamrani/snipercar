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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "filter_item", uniqueConstraints={ @UniqueConstraint(columnNames = {"filter_id", "item_id"}) }) 
@Getter
@Setter
@ToString//(exclude = { "users" })
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class FilterItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "filter_id")
	private Filter filter;
	
	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(name = "first_parse")
	private Boolean firstParse = true;
	
	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt = new Date();

	@Column(name = "updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt = new Date();

	public FilterItem(Filter filter, Item item) {
		this.filter = filter;
		this.item = item;
		this.firstParse = !filter.getParsed();
	}

}
