package com.codevo.snipercar.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "selection")
@Getter
@Setter
@ToString(exclude = { "filters" })
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Selection {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

//	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

//	@JsonBackReference
//	@OneToMany(mappedBy = "currentSelection")
//	private List<User> currentUsers;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "selection_filters", 
			joinColumns = { @JoinColumn(name = "selection_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "filter_id") })
	private Set<Filter> filters = new HashSet<>();
	
	@Column(name = "is_default", nullable = false)
	private Boolean isDefault = false;

//	public Selection() {
//		this.filters = new HashSet<>();
//	}

}
