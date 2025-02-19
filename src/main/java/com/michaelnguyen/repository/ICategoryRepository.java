package com.michaelnguyen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.michaelnguyen.entity.Category;

public interface ICategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByParentCategoryId(Long parentId);

	List<Category> findByParentCategoryIsNull();

}
