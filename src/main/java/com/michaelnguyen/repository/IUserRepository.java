package com.michaelnguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.michaelnguyen.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	Boolean existsByEmail(String email);

	User findByGoogleId(String googleId);
	
	@Transactional
	@Modifying
	@Query("update User u set u.enabled = true where u.id = :id")
	void updateEnabled(@Param(value = "id") long id);

}
