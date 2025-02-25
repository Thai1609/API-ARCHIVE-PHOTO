package com.michaelnguyen.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.michaelnguyen.dto.request.GalleryRequest;
import com.michaelnguyen.dto.request.ProductRequest;
import com.michaelnguyen.dto.response.GalleryResponse;
import com.michaelnguyen.entity.Product;
import com.michaelnguyen.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	@Autowired
	private ProductService productService;

	// Lấy danh sách tất cả sản phẩm
	@GetMapping
	public Page<Product> getProducts(@RequestParam(required = false) String name,
			@RequestParam(required = false) Long categoryId, @RequestParam(required = false) Long parentId,
			@RequestParam(required = false) Long brandId, @RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);

		return productService.getProducts(name, categoryId, parentId, brandId, minPrice, maxPrice, pageable);
	}

	// Lấy sản phẩm theo ID
	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		Optional<Product> product = productService.getProductById(id);
		return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// Thêm sản phẩm mới
//	@PostMapping
//	public ResponseEntity<Product> addProduct(@RequestBody Product product) {
//		Product savedProduct = productService.addProduct(product);
//		return ResponseEntity.ok(savedProduct);
//	}
	@PostMapping(value = "/upload-image", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public GalleryResponse createProduct(@RequestPart("file") List<MultipartFile> multipartFile,
			@RequestPart ProductRequest request) {

		return galleryService.uploadImage(multipartFile, request);
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
