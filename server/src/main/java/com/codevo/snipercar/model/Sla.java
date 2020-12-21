package com.codevo.snipercar.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;

import lombok.*;

import com.fasterxml.jackson.annotation.*;

@Entity
@Table(name = "sla")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Sla {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", nullable = false)
	@NotBlank(message = "Name is mandatory")
	private String name;

	@Lob
	@Column(name = "description", nullable = true)
	private String description;

	/*
	@Column(name = "latency", columnDefinition = "UNSIGNED INT(11)", nullable = false)
	@NotNull(message = "Latency is mandatory")
	@Min(0)
	private Integer latency;

	@Column(name = "price", columnDefinition = "UNSIGNED INT(11)", nullable = false)
	@NotNull(message = "Latency is mandatory")
	@Min(0)
	private Integer price;
	*/
	@Column(name = "latency", nullable = false)
	private Integer latency;

	@Column(name = "price", nullable = false)
	private Integer price;
	
	/// abde modification done


//	public Sla() {
//
//	}
}
