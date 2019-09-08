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

import lombok.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "company")
@Getter
@Setter
@ToString//(exclude = { "users" })
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "phone", nullable = false)
	private String phone;

//	@OneToMany(mappedBy = "company")
//	private List<User> users;

	@ManyToOne
	@JoinColumn(name = "sla_id")
	private Sla sla;

	@Column(name = "expiration")
	@JsonFormat(pattern="yyyy-MM-dd")
	//@Temporal(TemporalType.TIMESTAMP)
	private Date expiration;

	@Column(name = "active", nullable = false)
	private Boolean active;

//	public Company() {
////		this.users = new ArrayList<>();
//	}

}
