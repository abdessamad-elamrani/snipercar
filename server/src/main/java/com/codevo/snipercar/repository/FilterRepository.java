package com.codevo.snipercar.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.*;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long>{
	
	@Query(""
			+ "SELECT f "
			+ "FROM Filter f "
			+ "WHERE "
			+ "	f.name LIKE CONCAT('%',:name,'%')")
	Page<Filter> findAllForDatatables(Pageable pageable, @Param("name") String name);

	List<Filter> findByWebsite(Website website);
	
}