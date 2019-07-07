package com.codevo.catchit.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
@Table(name = "filter")
public class Filter {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@ManyToOne
	@JoinColumn(name="website_id")
    private Website website;
	
	@OneToMany(mappedBy = "filter")
    private List<Field> fields;
	
	@Column(name = "url", nullable = false)
    private String url;
    
    public Filter() {
    	  this.fields = new ArrayList<>();
    }
 
    public Filter(Website website, String url) {
         this.website = website;
         this.fields = new ArrayList<>();
         this.url = url;
    }
 
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
 
    public Website getWebsite() {
        return website;
    }
    public void setWebsite(Website website) {
        this.website = website;
    }
 
    public List<Field> getFields() {
        return fields;
    }
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
    public void addField(Field field) {
        fields.add(field);
        field.setFilter(this);
    }
    public void removeComment(Field field) {
        fields.remove(field);
        field.setFilter(null);
    }
 
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Filter [" 
		    + "id=" + id 
		    + ", website=" + website 
		    + ", fields=" + fields 
		    + ", url=" + url 
	    + "]";
    }

}
