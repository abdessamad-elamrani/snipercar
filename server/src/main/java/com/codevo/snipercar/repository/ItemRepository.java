package com.codevo.snipercar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.Filter;
import com.codevo.snipercar.model.Item;
import com.codevo.snipercar.model.User;
import com.codevo.snipercar.model.Website;;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{

	@Query(""
			+ " SELECT i"
			+ " FROM Item i"
			+ " LEFT JOIN i.filterItems fi"
			+ " WHERE"
			+ "   fi.filter = :filter"
			+ " ORDER BY fi.createdAt DESC, fi.updatedAt DESC")
	List<Item> findByFilter(Filter filter);

	Item findByRef(String ref);
	
	Item findByRefAndWebsite(String ref, Website website);
	
	@Query(""
			+ " SELECT DISTINCT i"
			+ " FROM User u"
			+ " INNER JOIN u.currentSelection s"
			+ " INNER JOIN s.filters f"
			+ " INNER JOIN f.filterItems fi"
			+ " INNER JOIN fi.item i"
			+ " LEFT JOIN u.userItems ui WITH ui.item = i"
			+ " INNER JOIN u.company c"
			+ " INNER JOIN c.sla sla"
			+ " WHERE"
			+ "   u = :agent"
			+ "   AND fi.createdAt <= SQL('(sysdaste - interval sla.latency minute)')"
			+ "   AND fi.createdAt >= u.currentSelectionStart"
			+ "   AND ui.id IS NULL"
			+ " ORDER BY fi.createdAt ASC, fi.updatedAt ASC"
			)
	List<Item> findAgentPendingItems(User agent);
	
//	@Query(""
//			+ "SELECT i "
//			+ "FROM Item i "
//			+ "WHERE "
//			+ "	 i.filter = :filter "
//			+ "ORDER BY i.createdAt DESC, i.updatedAt DESC ")
//	List<Item> purgeItems();

}