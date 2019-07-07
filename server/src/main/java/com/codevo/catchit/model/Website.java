package com.codevo.catchit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "website")
public class Website {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(name = "name", nullable = false)
    private String name;
	
	@Column(name = "url", nullable = false)
    private String url;
    
    public Website() {
    	  
    }
 
    public Website(String name, String url) {
         this.name = name;
         this.url = url;
    }
 
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
  
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
 
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Website ["
		    + "id=" + id 
		    + ", name=" + name 
		    + ", url=" + url 
	    + "]";
    }

}
