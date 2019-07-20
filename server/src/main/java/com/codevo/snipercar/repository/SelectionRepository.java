package com.codevo.snipercar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.Selection;

@Repository
public interface SelectionRepository extends JpaRepository<Selection, Long>{

}