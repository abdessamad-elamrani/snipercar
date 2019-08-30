package com.codevo.snipercar.repository;

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
			+ "SELECT c "
			+ "FROM Company c "
			+ "WHERE "
			+ "	c.name LIKE CONCAT('%',:name,'%')")
	Page<Company> findAllForDatatables(Pageable pageable, @Param("name") String name);

}