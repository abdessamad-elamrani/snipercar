package com.codevo.catchit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.catchit.model.Website;;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Long>{

}