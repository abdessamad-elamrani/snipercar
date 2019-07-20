package com.codevo.snipercar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.Filter;
import com.codevo.snipercar.model.Website;;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long>{

	List<Filter> findByWebsite(Website website);

}