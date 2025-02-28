package com.michaelnguyen.service;

import com.michaelnguyen.entity.Category;
import com.michaelnguyen.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public Category createCategory(String name, Long parentId) {
        Category category = new Category();
        category.setName(name);

        if (parentId != null) {
            Category parentCategory = categoryRepository.findById(parentId).orElseThrow(() -> new RuntimeException("Parent category not found with id: " + parentId));
            category.setParentCategory(parentCategory);
        }

        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, String name, Long parentId) {
        Category category = getCategoryById(id);
        category.setName(name);

        if (parentId != null) {
            Category parentCategory = categoryRepository.findById(parentId).orElseThrow(() -> new RuntimeException("Parent category not found with id: " + parentId));
            category.setParentCategory(parentCategory);
        } else {
            category.setParentCategory(null);
        }

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    public List<Category> getSubCategories(Long parentId) {
        return categoryRepository.findByParentCategoryId(parentId);
    }

}
