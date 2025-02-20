package com.michaelnguyen.dto.request;

import com.michaelnguyen.entity.Product;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
	public static Specification<Product> filterProducts(String name, Long categoryId, Long parentId, Long brandId,
			Double minPrice, Double maxPrice) {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();

			if (name != null && !name.isEmpty()) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + name + "%"));
			}
			
			if (categoryId != null && parentId == null) {
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.equal(root.join("category").get("parentCategory").get("id"), categoryId));
			}
			
			if (parentId != null) {
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.equal(root.get("category").get("id"), parentId));
			}

			if (brandId != null) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("brand").get("id"), brandId));
			}
			
			if (minPrice != null) {
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
			}
			
			if (maxPrice != null) {
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
			}

			return predicate;
		};
	}
}
