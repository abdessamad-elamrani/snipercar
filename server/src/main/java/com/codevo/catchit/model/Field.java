package com.codevo.catchit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "field")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
	@JoinColumn(name="filter_id")
    private Filter filter;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "value", nullable = false)
    private String value;
    
    public Field() {
    	  
    }
 
    public Field(Filter filter, String name, String value) {
        this.filter = filter;
        this.name = name;
        this.value = value;
    }
 
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
 
    public Filter getFilter() {
        return filter;
    }
    public void setFilter(Filter filter) {
        this.filter = filter;
    }
 
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
 
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Field [" 
		    + "id=" + id 
		    + ", filter=" + filter 
		    + ", name=" + name 
		    + ", value=" + value 
	    + "]";
    }
    
}
