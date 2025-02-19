package com.michaelnguyen.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.michaelnguyen.entity.Product;
import com.michaelnguyen.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	@Autowired
	private ProductService productService;

	// Lấy danh sách tất cả sản phẩm
	@GetMapping
	public Page<Product> getProducts(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return productService.getProducts(page, size);
	}

	// Lấy sản phẩm theo ID
	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		Optional<Product> product = productService.getProductById(id);
		return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// Thêm sản phẩm mới
	@PostMapping
	public ResponseEntity<Product> addProduct(@RequestBody Product product) {
		Product savedProduct = productService.addProduct(product);
		return ResponseEntity.ok(savedProduct);
	}

	// Cập nhật sản phẩm theo ID
	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
		Optional<Product> updatedProduct = productService.updateProduct(id, productDetails);
		return updatedProduct.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// Xóa sản phẩm theo ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}
