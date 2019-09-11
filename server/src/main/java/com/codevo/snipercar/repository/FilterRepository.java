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
public interface FilterRepository extends JpaRepository<Filter, Long>{
	
	@Query(""
			+ " SELECT"
			+ "   f.id          AS id,"
			+ "   f.name        AS name,"
			+ "   f.description AS description,"
			+ "   w.name        AS websiteName"
			+ " FROM Filter f"
			+ " LEFT JOIN f.website w"
			+ " WHERE"
			+ " 	 f.name LIKE CONCAT('%',:name,'%')")
	Page<Map<String, String>> findAllForDatatables(Pageable pageable, @Param("name") String name);

	List<Filter> findByWebsite(Website website);
	
//	@Modifying
//    @Transactional
//    @Query("DELETE FROM selection_filters sf WHERE sf._filter_id = :filterId")
//    void clearAllSelections(@Param("filterId") String filterId);
}