package com.michaelnguyen.repository;

import com.michaelnguyen.entity.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductTagRepository extends JpaRepository<ProductTag, Long> {
}
