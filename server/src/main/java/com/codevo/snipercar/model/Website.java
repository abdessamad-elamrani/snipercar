package com.codevo.snipercar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.*;

import com.fasterxml.jackson.annotation.*;

@Entity
@Table(name = "website")
@Getter
@Setter
@ToString  
// (exclude = { "filters" })
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Website {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "url", nullable = false)
	private String url;

//	@OneToMany(mappedBy = "website")
//	private List<Filter> filters = new ArrayList<>();;

//	public Website() { 
////		this.filters = new ArrayList<>();
//	}
//
//	public Website(String name, String url) {
//		this.name = name;
//		this.url = url;
////		this.filters = new ArrayList<>();
//	}

}
