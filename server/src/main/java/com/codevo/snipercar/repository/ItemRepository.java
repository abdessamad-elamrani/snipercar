package com.codevo.snipercar.repository;

import java.util.Date;
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
			+ " INNER JOIN User admin ON admin.role LIKE '%ADMIN%'"
			+ " INNER JOIN Selection s ON"
			+ "   (u.currentSelection IS NOT NULL AND u.currentSelection = s)"
			+ "   OR (u.currentSelection IS NULL AND s.user = admin AND s.isDefault = 1)"
			+ " INNER JOIN s.filters f"
			+ " INNER JOIN f.filterItems fi"
			+ " INNER JOIN fi.item i"
//			+ " LEFT JOIN u.userItems ui WITH ui.item = i"
			+ " LEFT JOIN UserItem ui ON ui.user = u AND ui.item = i"
			+ " INNER JOIN u.company c"
			+ " INNER JOIN c.sla sla"
			+ " WHERE"
			+ "   u = :agent"
			+ "   AND fi.firstParse = 0"
			+ "   AND fi.createdAt BETWEEN u.currentSelectionStart AND :latencyDate"
			+ "   AND ui.id IS NULL"
			+ " ORDER BY fi.createdAt ASC, fi.updatedAt ASC"
			)
	List<Item> findAgentPendingItems(User agent, Date latencyDate); 
	
//	@Query(""
//			+ "SELECT i "
//			+ "FROM Item i "
//			+ "WHERE "
//			+ "	 i.filter = :filter "
//			+ "ORDER BY i.createdAt DESC, i.updatedAt DESC ")
//	List<Item> purgeItems();

}