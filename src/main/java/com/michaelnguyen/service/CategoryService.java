package com.michaelnguyen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.michaelnguyen.entity.Category;
import com.michaelnguyen.repository.ICategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private ICategoryRepository categoryRepository;

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
	}

	/**
	 * Thêm mới danh mục (có thể có danh mục cha)
	 */
	public Category createCategory(String name, Long parentId) {
		Category category = new Category();
		category.setName(name);

		if (parentId != null) {
			Category parentCategory = categoryRepository.findById(parentId)
					.orElseThrow(() -> new RuntimeException("Parent category not found with id: " + parentId));
			category.setParentCategory(parentCategory);
		}

		return categoryRepository.save(category);
	}

	/**
	 * Cập nhật danh mục
	 */
	public Category updateCategory(Long id, String name, Long parentId) {
		Category category = getCategoryById(id);
		category.setName(name);

		if (parentId != null) {
			Category parentCategory = categoryRepository.findById(parentId)
					.orElseThrow(() -> new RuntimeException("Parent category not found with id: " + parentId));
			category.setParentCategory(parentCategory);
		} else {
			category.setParentCategory(null);
		}

		return categoryRepository.save(category);
	}

	/**
	 * Xóa danh mục
	 */
	public void deleteCategory(Long id) {
		Category category = getCategoryById(id);
		categoryRepository.delete(category);
	}

	/**
	 * Lấy danh mục con của một danh mục cha
	 */
	public List<Category> getSubCategories(Long parentId) {
		return categoryRepository.findByParentCategoryId(parentId);
	}

	/**
	 * Lấy danh mục gốc (không có danh mục cha)
	 */
	public List<Category> getRootCategories() {
		return categoryRepository.findByParentCategoryIsNull();
	}
}
