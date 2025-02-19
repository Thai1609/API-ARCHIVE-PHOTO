package com.michaelnguyen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.michaelnguyen.dto.request.CategoryRequest;
import com.michaelnguyen.entity.Category;
import com.michaelnguyen.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public ResponseEntity<List<Category>> getAllCategories() {
		return ResponseEntity.ok(categoryService.getAllCategories());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
		return ResponseEntity.ok(categoryService.getCategoryById(id));
	}

	@PostMapping
	public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest request) {
		Category category = categoryService.createCategory(request.getName(), request.getParentId());
		return ResponseEntity.status(HttpStatus.CREATED).body(category);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request) {
		return ResponseEntity.ok(categoryService.updateCategory(id, request.getName(), request.getParentId()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/root")
	public ResponseEntity<List<Category>> getRootCategories() {
		return ResponseEntity.ok(categoryService.getRootCategories());
	}

	@GetMapping("/{parentId}/subcategories")
	public ResponseEntity<List<Category>> getSubCategories(@PathVariable Long parentId) {
		return ResponseEntity.ok(categoryService.getSubCategories(parentId));
	}
}
