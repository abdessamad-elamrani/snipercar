package com.codevo.snipercar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.Website;;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Long>{

}