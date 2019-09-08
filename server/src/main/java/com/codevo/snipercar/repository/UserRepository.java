package com.codevo.snipercar.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codevo.snipercar.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);

	@Query(""
			+ "SELECT u "
			+ "FROM User u "
			+ "WHERE"
			+ " u.role IN ('ADMIN', 'SUPER_ADMIN')"
			+ " AND u.name LIKE CONCAT('%',:name,'%')")
	Page<User> findAllAdminsForDatatables(Pageable pageable, @Param("name") String name);
	
	@Query(""
			+ "SELECT u "
			+ "FROM User u "
			+ "WHERE"
			+ " u.role IN ('AGENT', 'SUPER_AGENT')"
			+ " AND u.name LIKE CONCAT('%',:name,'%')")
	Page<User> findAllAgentsForDatatables(Pageable pageable, @Param("name") String name);
	
	@Query(""
			+ "SELECT u "
			+ "FROM User u "
			+ "INNER JOIN u.company c "
			+ "WHERE"
			+ " u.role IN ('AGENT', 'SUPER_AGENT')"
			+ " AND u.name LIKE CONCAT('%',:name,'%')"
			+ " AND c.id = :companyId")
	Page<User> findAllAgentsForDatatables(Pageable pageable, @Param("name") String name, @Param("companyId") Long companyId);


	@Query(""
			+ "SELECT u "
			+ "FROM User u "
			+ "WHERE"
			+ " u.role IN ('ADMIN', 'SUPER_ADMIN')"
			+ " AND u.id = :id")
	Optional<User> findAdminById(@Param("id") Long id);
	
	@Query(""
			+ "SELECT u "
			+ "FROM User u "
			+ "WHERE"
			+ " u.role IN ('AGENT', 'SUPER_AGENT')"
			+ " AND u.id = :id")
	Optional<User> findAgentById(@Param("id") Long id);
	
	@Query(""
			+ "SELECT u.username "
			+ "FROM User u "
			+ "WHERE u.id <> :id")
	List<String> findAllReservedUsernames(@Param("id") Long id);

}