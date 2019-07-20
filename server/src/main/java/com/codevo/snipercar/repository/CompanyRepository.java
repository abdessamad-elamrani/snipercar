package com.codevo.snipercar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{

}