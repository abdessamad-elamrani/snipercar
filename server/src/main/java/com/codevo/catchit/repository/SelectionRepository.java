package com.codevo.catchit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.catchit.model.Selection;

@Repository
public interface SelectionRepository extends JpaRepository<Selection, Long>{

}