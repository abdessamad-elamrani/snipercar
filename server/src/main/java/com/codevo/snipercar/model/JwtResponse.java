package com.codevo.snipercar.model;

import java.io.Serializable;
import java.util.Set;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String token;
	private final String tokenRefresh;
	private final Long expiration;
	private final Set roles;
	private final User user;
}
