package com.codevo.snipercar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.Sla;

@Repository
public interface SlaRepository extends JpaRepository<Sla, Long>{

}