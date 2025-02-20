package com.michaelnguyen.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.michaelnguyen.dto.request.ProductSpecification;
import com.michaelnguyen.entity.Product;
import com.michaelnguyen.repository.IProductRepository;

@Service
public class ProductService {
	@Autowired
	private IProductRepository productRepository;

	public Page<Product> getProducts(String name, Long categoryId, Long parentId, Long brandId, Double minPrice,
			Double maxPrice, Pageable pageable) {

		return productRepository.findAll(
				ProductSpecification.filterProducts(name, categoryId, parentId, brandId, minPrice, maxPrice), pageable);
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
