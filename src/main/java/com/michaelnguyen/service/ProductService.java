package com.michaelnguyen.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.michaelnguyen.dto.request.ProductSpecification;
import com.michaelnguyen.dto.request.ProductUploadRequest;
import com.michaelnguyen.dto.response.ProductResponse;
import com.michaelnguyen.entity.Brand;
import com.michaelnguyen.entity.Category;
import com.michaelnguyen.entity.Product;
import com.michaelnguyen.entity.ProductImage;
import com.michaelnguyen.entity.User;
import com.michaelnguyen.firebase.UploadService;
import com.michaelnguyen.mapper.IProductMapper;
import com.michaelnguyen.repository.IBrandRepository;
import com.michaelnguyen.repository.ICategoryRepository;
import com.michaelnguyen.repository.IProductImageRepository;
import com.michaelnguyen.repository.IProductRepository;
import com.michaelnguyen.repository.IUserRepository;

@Service
public class ProductService {
	@Autowired
	private IProductRepository productRepository;
	@Autowired
	private IUserRepository iUserRepository;
	@Autowired
	private ICategoryRepository iCategoryRepository;
	@Autowired
	private IBrandRepository iBrandRepository;
	@Autowired
	private IProductImageRepository iProductImageRepository;
	@Autowired
	private UploadService uploadService;

	@Autowired
	private IProductMapper iProductMapper;

	public Page<Product> getProducts(String name, Long categoryId, Long parentId, Long brandId, Double minPrice,
			Double maxPrice, Pageable pageable) {

		return productRepository.findAll(
				ProductSpecification.filterProducts(name, categoryId, parentId, brandId, minPrice, maxPrice), pageable);
	}

	// Lấy sản phẩm theo ID
	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
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

	public ProductResponse uploadProduct(List<MultipartFile> multipartFiles, ProductUploadRequest request) {

		Optional<User> user = iUserRepository.findById(request.getUserId());

		Optional<Category> category = iCategoryRepository.findById(request.getCategoryId());

		Brand brand = request.getBrandId() != null ? iBrandRepository.findById(request.getBrandId()).orElse(null)
				: null;

		Product product = new Product();
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setDiscount(request.getDiscount());
		product.setStock(request.getStock());

		product.setUser(user.get());
		product.setCategory(category.get());
		product.setBrand(brand);

		Product newProduct = productRepository.save(product);

		// check brand
		String brandName = (brand != null) ? brand.getName() : "NoBrand";

		if (multipartFiles != null && !multipartFiles.isEmpty()) {
			for (MultipartFile file : multipartFiles) {

				String uploadPath = "products/" + category.get().getName() + "/" + brandName + "/";
				// save productImage in database
				ProductImage productImage = new ProductImage();
				productImage.setProduct(newProduct);
				productImage.setImageUrl(uploadService.upload(file, uploadPath));
				iProductImageRepository.save(productImage);
			}
		}

		productRepository.save(newProduct);

		return iProductMapper.toProductResponse(newProduct);
	}
}
