package com.codevo.snipercar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.Filter;
import com.codevo.snipercar.model.Item;;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{

	List<Item> findByFilter(Filter filter);

	Item findByRef(String ref);
	
	Item findByRefAndFilter(String ref, Filter filter);

}