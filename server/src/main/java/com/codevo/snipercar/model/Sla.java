package com.codevo.snipercar.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "sla")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Sla {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "latency", nullable = false)
	private Integer latency;

	@Column(name = "price", nullable = false)
	private Integer price;

	public Sla() {

	}
}
