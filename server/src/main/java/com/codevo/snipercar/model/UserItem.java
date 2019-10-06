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
import javax.persistence.Lob;
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
@Table(name = "user_item", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "item_id" }) })
@Getter
@Setter
@ToString // (exclude = { "users" })
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(name = "phone", nullable = true)
	private String phone;
	
	@Column(name = "sms_notif")
	private Boolean smsNotif = false;
	
	@Column(name = "sms_sent")
	private Boolean smsSent = false;
	
	@Lob
	@Column(name = "sms_log", nullable = true)
	private String smsLog;

	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "email_notif")
	private Boolean emailNotif = false;
	
	@Column(name = "email_sent")
	private Boolean emailSent = false;
	
	@Lob
	@Column(name = "email_log", nullable = true)
	private String emailLog;

	@Column(name = "sent_at")
	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date sentAt = new Date();

	public UserItem(User user, Item item) {
		this.user = user;
		this.item = item;
		this.phone = user.getPhone();
		this.smsNotif = user.getSmsNotif();
		this.email = user.getEmail();
		this.emailNotif = user.getEmailNotif();
	}

}
