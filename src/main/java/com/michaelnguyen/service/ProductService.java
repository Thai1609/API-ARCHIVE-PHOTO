package com.michaelnguyen.service;

import com.michaelnguyen.dto.request.ProductSpecification;
import com.michaelnguyen.dto.request.ProductUploadRequest;
import com.michaelnguyen.dto.response.ProductResponse;
import com.michaelnguyen.entity.*;
import com.michaelnguyen.firebase.UploadService;
import com.michaelnguyen.mapper.IProductMapper;
import com.michaelnguyen.repository.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final IProductRepository iProductRepository;
    private final IUserRepository iUserRepository;
    private final ICategoryRepository iCategoryRepository;
    private final IBrandRepository iBrandRepository;
    private final IProductImageRepository iProductImageRepository;
    private final UploadService uploadService;
    @Qualifier("IProductMapper")
    private IProductMapper iProductMapper;

    public ProductService(IProductRepository iProductRepository, IUserRepository iUserRepository, ICategoryRepository iCategoryRepository, IBrandRepository iBrandRepository, IProductImageRepository iProductImageRepository, UploadService uploadService) {
        this.iProductRepository = iProductRepository;
        this.iUserRepository = iUserRepository;
        this.iCategoryRepository = iCategoryRepository;
        this.iBrandRepository = iBrandRepository;
        this.iProductImageRepository = iProductImageRepository;
        this.uploadService = uploadService;
    }


    public Page<Product> getProducts(String name, Long categoryId, Long parentId, Long brandId, Double minPrice,
                                     Double maxPrice, Pageable pageable) {

        return iProductRepository.findAll(
                ProductSpecification.filterProducts(name, categoryId, parentId, brandId, minPrice, maxPrice), pageable);
    }

    // Lấy sản phẩm theo ID
    public Optional<Product> getProductById(Long id) {
        return iProductRepository.findById(id);
    }

    // Cập nhật sản phẩm
    public Optional<Product> updateProduct(Long id, Product productDetails) {
        return iProductRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setDiscount(productDetails.getDiscount());
            product.setStock(productDetails.getStock());
            product.setCategory(productDetails.getCategory());
            product.setBrand(productDetails.getBrand());
            return iProductRepository.save(product);
        });
    }

    // Xóa sản phẩm theo ID
    public void deleteProduct(Long id) {
        iProductRepository.deleteById(id);
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

        Product newProduct = iProductRepository.save(product);

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

        iProductRepository.save(newProduct);

        return iProductMapper.toProductResponse(newProduct);
    }
}
