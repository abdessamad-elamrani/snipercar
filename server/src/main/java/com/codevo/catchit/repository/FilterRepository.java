package com.codevo.catchit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.catchit.model.Filter;;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long>{

}