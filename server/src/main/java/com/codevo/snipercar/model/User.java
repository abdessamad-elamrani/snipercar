package com.codevo.snipercar.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString(exclude = { "selections" })
@EqualsAndHashCode
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "salt", nullable = false)
	private String salt;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "firstname", nullable = false)
	private String firstname;

	@Column(name = "lastname", nullable = false)
	private String lastname;

	@Column(name = "role", nullable = false)
	private String role;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "phone", nullable = true)
	private String phone;

	@Column(name = "smsNotif", nullable = false)
	private Boolean smsNotif;

	@Column(name = "emailNotif", nullable = false)
	private Boolean emailNotif;

	@OneToMany(mappedBy = "user")
	private List<Selection> selections;

	@OneToOne
	@JoinColumn(name = "current_selection_id", referencedColumnName = "id", nullable = true)
	private Selection currentSelection;

	@Column(name = "active", nullable = false)
	private Boolean active;

	public User() {
		this.selections = new ArrayList<>();
	}

}
