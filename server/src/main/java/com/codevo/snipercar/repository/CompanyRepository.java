package com.codevo.snipercar.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{
	
	@Query(""
			+ " SELECT"
			+ "   c.id         AS id,"
			+ "   c.name       AS name,"
			+ "   c.email      AS email,"
			+ "   c.phone      AS phone,"
			+ "   s.name       AS slaName,"
			+ "   c.active     AS active,"
			+ "   c.expiration AS expiration"
			+ " FROM Company c"
			+ " LEFT JOIN c.sla s"
			+ " WHERE"
			+ " 	 c.name LIKE CONCAT('%',:name,'%')")
	Page<Map<String, String>> findAllForDatatables(Pageable pageable, @Param("name") String name);

}