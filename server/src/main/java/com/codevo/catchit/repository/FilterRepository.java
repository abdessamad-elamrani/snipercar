package com.codevo.catchit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.catchit.model.Filter;
import com.codevo.catchit.model.Website;;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long>{

	List<Filter> findByWebsite(Website website);

}