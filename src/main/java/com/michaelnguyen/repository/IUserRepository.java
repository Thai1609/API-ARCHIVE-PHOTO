package com.michaelnguyen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.michaelnguyen.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE (  u.email = :email) AND ( u.provider = :provider) "
			+ "AND ( u.providerId = :providerId)")
	Optional<User> findByOptions(@Param("email") String email, @Param("provider") String provider,
			@Param("providerId") String providerId);

	@Transactional
	@Modifying
	@Query("update User u set u.enabled = true where u.id = :id")
	void updateEnabled(@Param(value = "id") long id);

}
