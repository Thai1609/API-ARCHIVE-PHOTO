package com.michaelnguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.michaelnguyen.entity.Tag;

@Repository
public interface ITagRepository extends JpaRepository<Tag, Integer> {

}
