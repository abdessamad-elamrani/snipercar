package com.codevo.catchit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.catchit.model.Field;;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long>{

}