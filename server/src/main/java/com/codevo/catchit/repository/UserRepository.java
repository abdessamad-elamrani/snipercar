package com.codevo.catchit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codevo.catchit.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}