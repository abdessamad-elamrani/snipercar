package com.codevo.snipercar.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.*;

@Repository
public interface SlaRepository extends JpaRepository<Sla, Long>{
	
	@Query(""
			+ " SELECT"
			+ "   s.id          AS id,"
			+ "   s.name        AS name,"
			+ "   s.description AS description,"
			+ "   s.latency     AS latency,"
			+ "   s.price       AS price"
			+ " FROM Sla s"
			+ " WHERE"
			+ " 	 s.name LIKE CONCAT('%',:name,'%')"
			+ " 	 AND s.description LIKE CONCAT('%',:description,'%')")
	Page<Map<String, String>> findAllForDatatables(Pageable pageable, @Param("name") String name, @Param("description") String description);
	
	@Query(""
			+ " SELECT s.id, s.name"
			+ " FROM Sla s")
	List<Sla> findAllForSelect2();

}