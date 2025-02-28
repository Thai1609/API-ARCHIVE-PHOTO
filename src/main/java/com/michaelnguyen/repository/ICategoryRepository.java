package com.michaelnguyen.repository;

import com.michaelnguyen.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentCategoryId(Long parentId);

}
