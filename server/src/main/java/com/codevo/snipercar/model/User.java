package com.codevo.snipercar.model;

import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.*;

import com.fasterxml.jackson.annotation.*;
import com.google.gson.annotations.Expose;
//import org.fasterxml.jackson.annotate.JsonIgnore;
//import org.codehaus.jackson.annotate.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString(exclude = { "selections" })
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "salt", nullable = false)
	private String salt = "snipercar_salt";

	@JsonIgnore
	@Column(name = "password", nullable = false)
	private String password;
	@Transient
	private Boolean passwordChange = false;
	@Transient
	private String newPassword;
	@Transient
	private String newPasswordConfirm;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "role", nullable = false)
	private String role;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "phone", nullable = true)
	private String phone;

	@Column(name = "smsNotif", nullable = false)
	private Boolean smsNotif = false;

	@Column(name = "emailNotif", nullable = false)
	private Boolean emailNotif = false;

	@JsonBackReference
	@OneToMany(mappedBy = "user")
	private List<Selection> selections;

	@JsonBackReference
	@OneToOne
	@JoinColumn(name = "current_selection_id", referencedColumnName = "id", nullable = true)
	private Selection currentSelection;

	@Column(name = "active", nullable = false)
	private Boolean active = true;

//	public User() {
//		this.selections = new ArrayList<>();
//		this.username = "xxx";
//		this.email = "xxx";
//		this.salt = "xxx";
//		this.password = "xxx";
//		this.name = "xxx";
//		this.role = "ROLE_USER";
//		this.company = null;
//		this.phone = "xxx";
//		this.smsNotif = true;
//		this.emailNotif = true;
//		this.currentSelection = null;
//		this.active = true;
//	}

}
