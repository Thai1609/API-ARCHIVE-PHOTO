package com.michaelnguyen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.michaelnguyen.entity.Tag;

@Repository
public interface ITagRepository extends JpaRepository<Tag, Integer> {
	@Query("SELECT u FROM Tag u WHERE u.name = ?1")
	Optional<Tag> findTagByName(String name);
}
