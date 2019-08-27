package com.codevo.snipercar.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DatatablesRequest implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	private int draw;
	private int start;
	private int length;
	private ArrayList<?> columns;
	private ArrayList<?> order;
    private Map<String, String> search;
    private Map<String, String> filter;

}
