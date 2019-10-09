package com.codevo.snipercar.repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.*;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {
	
	@Query(""
			+ " SELECT"
			+ "   ui.id         AS id,"
			+ "   c.name        AS companyName,"
			+ "   a.name        AS agentName,"
			+ "   i.ref         AS itemRef,"
			+ "   i.title       AS itemTitle,"
			+ "   i.url         AS itemUrl,"
			+ "   ui.phone      AS phone,"
			+ "   ui.smsNotif   AS smsNotif,"
			+ "   ui.smsSent    AS smsSent,"
			+ "   ui.smsLog     AS smsLog,"
			+ "   ui.email      AS email,"
			+ "   ui.emailNotif AS emailNotif,"
			+ "   ui.emailSent  AS emailSent,"
			+ "   ui.emailLog   AS emailLog,"
			+ "   ui.createdAt  AS createdAt"
			+ " FROM UserItem ui"
			+ " LEFT JOIN ui.item i"
			+ " LEFT JOIN ui.user a"
			+ " LEFT JOIN a.company c"
			+ " WHERE"
			+ "   (c.id = :companyId OR :companyId = 0)"
			+ "   AND (a.id = :agentId OR :agentId = 0)"
			+ "   AND i.title LIKE CONCAT('%', :itemTitle, '%')"
			+ " ORDER BY ui.createdAt DESC")
	Page<Map<String, String>> findAllForDatatables(
			Pageable pageable, 
			@Param("companyId") Long companyId,
			@Param("agentId") Long agentId,
			@Param("itemTitle") String itemTitle
	); 

}