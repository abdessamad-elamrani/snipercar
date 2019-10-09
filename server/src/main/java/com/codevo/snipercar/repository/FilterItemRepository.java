package com.codevo.snipercar.repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.*;

@Repository
public interface FilterItemRepository extends JpaRepository<FilterItem, Long> {
	
	@Query(""
			+ " SELECT"
			+ "   fi.id         AS id,"
			+ "   w.name        AS websiteName,"
			+ "   f.name        AS filterName,"
			+ "   i.ref         AS itemRef,"
			+ "   i.title       AS itemTitle,"
			+ "   i.url         AS itemUrl,"
			+ "   fi.firstParse AS firstParse,"
			+ "   fi.createdAt  AS createdAt,"
			+ "   fi.updatedAt  AS updatedAt"
			+ " FROM FilterItem fi"
			+ " LEFT JOIN fi.item i"
			+ " LEFT JOIN fi.filter f"
			+ " LEFT JOIN f.website w"
			+ " WHERE"
			+ "   (w.id = :websiteId OR :websiteId = 0)"
			+ "   AND (f.id = :filterId OR :filterId = 0)"
			+ "   AND i.title LIKE CONCAT('%', :itemTitle, '%')"
			+ " ORDER BY fi.createdAt DESC")
	Page<Map<String, String>> findAllForDatatables(
			Pageable pageable, 
			@Param("websiteId") Long websiteId,
			@Param("filterId") Long filterId,
			@Param("itemTitle") String itemTitle
	); 


	FilterItem findByFilterAndItem(Filter filter, Item item);
	
}