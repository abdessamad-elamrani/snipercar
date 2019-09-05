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
	
	@Query(""
			+ "SELECT u "
			+ "FROM User u "
			+ "WHERE"
			+ " u.role IN ('ADMIN', 'SUPER_ADMIN')"
			+ " AND u.name LIKE CONCAT('%',:name,'%')")
	Page<User> findAllAdminsForDatatables(Pageable pageable, @Param("name") String name);

	User findByUsername(String username);

	@Query(""
			+ "SELECT u "
			+ "FROM User u "
			+ "WHERE"
			+ " u.role IN ('ADMIN', 'SUPER_ADMIN')"
			+ " AND u.id = :id")
	Optional<User> findAdminById(@Param("id") Long id);
	
	@Query(""
			+ "SELECT u.username "
			+ "FROM User u "
			+ "WHERE u.id <> :id")
	List<Set> findAllReservedUsernames(@Param("id") Long id);

}