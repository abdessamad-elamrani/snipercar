package com.codevo.catchit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.catchit.model.Sla;

@Repository
public interface SlaRepository extends JpaRepository<Sla, Long>{

}