package com.michaelnguyen.repository;

import com.michaelnguyen.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findAll(Pageable pageable);

    List<Product> findProductByUser_Id(Long userId);

    List<Product> findProductByCategory_Id(Long categoryId);
}
