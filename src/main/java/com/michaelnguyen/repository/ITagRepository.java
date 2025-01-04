package com.michaelnguyen.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.michaelnguyen.entity.Tag;

import jakarta.transaction.Transactional;

@Repository
public interface ITagRepository extends JpaRepository<Tag, Integer> {
	@Query("SELECT u FROM Tag u WHERE u.name = ?1")
	Optional<Tag> findTagByName(String name);

	@Query("SELECT u FROM Tag u WHERE  u.user.id = :userId")
	List<Tag> findByOptions(@Param("userId") Long userId, Sort sort);

	@Modifying
	@Transactional
	@Query("DELETE FROM Tag u WHERE u.name = ?1")
	void deleteTagByName(String name);

}
