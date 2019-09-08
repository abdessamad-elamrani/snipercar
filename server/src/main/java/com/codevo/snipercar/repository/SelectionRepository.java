package com.codevo.snipercar.repository;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.*;

@Repository
public interface SelectionRepository extends JpaRepository<Selection, Long>{
	
	@Query(""
			+ "SELECT s "
			+ "FROM Selection s "
			+ "WHERE "
			+ "	s.name LIKE CONCAT('%',:name,'%')")
	Page<Selection> findAllForDatatables(Pageable pageable, @Param("name") String name);
	
	@Query(""
			+ "SELECT s.id, s.name "
			+ "FROM Selection s ")
	List<Selection> findAllForSelect2();
	
	@Query(""
			+ "SELECT s "
			+ "FROM Selection s "
			+ "INNER JOIN s.user u "
			+ "WHERE "
			+ " u.role LIKE '%ADMIN%' "
			+ "	OR u.id = :agentId")
	List<Selection> findByAgentId(@Param("agentId") Long agentId);

}