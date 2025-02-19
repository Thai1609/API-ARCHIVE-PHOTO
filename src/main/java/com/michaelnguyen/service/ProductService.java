package com.michaelnguyen.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.michaelnguyen.entity.Product;
import com.michaelnguyen.repository.IProductRepository;

@Service
public class ProductService {
	@Autowired
	private IProductRepository productRepository;

	// Lấy danh sách tất cả sản phẩm
	public Page<Product> getProducts(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return productRepository.findAll(pageable);
	}

	// Lấy sản phẩm theo ID
	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}

	// Thêm sản phẩm mới
	public Product addProduct(Product product) {
		return productRepository.save(product);
	}

	// Cập nhật sản phẩm
	public Optional<Product> updateProduct(Long id, Product productDetails) {
		return productRepository.findById(id).map(product -> {
			product.setName(productDetails.getName());
			product.setDescription(productDetails.getDescription());
			product.setPrice(productDetails.getPrice());
			product.setDiscount(productDetails.getDiscount());
			product.setStock(productDetails.getStock());
			product.setCategory(productDetails.getCategory());
			product.setBrand(productDetails.getBrand());
			return productRepository.save(product);
		});
	}

	// Xóa sản phẩm theo ID
	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
}
