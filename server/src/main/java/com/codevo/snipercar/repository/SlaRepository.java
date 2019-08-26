package com.codevo.snipercar.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.Sla;

@Repository
public interface SlaRepository extends JpaRepository<Sla, Long>{
	
	@Query(""
			+ "SELECT s "
			+ "FROM Sla s "
			+ "WHERE "
			+ "	s.name LIKE CONCAT('%',:name,'%')"
			+ "	AND s.description LIKE CONCAT('%',:description,'%')")
	Page<Sla> findSlaForDatatables(Pageable pageable, @Param("name") String name, @Param("description") String description);

}