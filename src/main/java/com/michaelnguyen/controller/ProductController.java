package com.michaelnguyen.controller;

import com.michaelnguyen.dto.request.ProductUploadRequest;
import com.michaelnguyen.dto.response.ProductResponse;
import com.michaelnguyen.entity.Product;
import com.michaelnguyen.mapper.IProductMapper;
import com.michaelnguyen.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    private final IProductMapper iProductMapper;

    public ProductController(ProductService productService, IProductMapper iProductMapper) {
        this.productService = productService;
        this.iProductMapper = iProductMapper;
    }

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
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(p -> ResponseEntity.ok(iProductMapper.toProductResponse(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Thêm sản phẩm mới
//	@PostMapping
//	public ResponseEntity<Product> addProduct(@RequestBody Product product) {
//		Product savedProduct = productService.addProduct(product);
//		return ResponseEntity.ok(savedProduct);
//	}
    @PostMapping(value = "/upload-product", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ProductResponse createProduct(@RequestPart("file") List<MultipartFile> multipartFile,
                                         @RequestPart ProductUploadRequest request) {

        return productService.uploadProduct(multipartFile, request);
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
