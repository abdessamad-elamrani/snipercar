package com.codevo.catchit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.catchit.model.Filter;
import com.codevo.catchit.model.Item;;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{

	List<Item> findByFilter(Filter filter);


}