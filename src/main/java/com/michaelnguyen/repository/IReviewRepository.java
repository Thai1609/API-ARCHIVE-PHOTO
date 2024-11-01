package com.michaelnguyen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.michaelnguyen.entity.Review;

public interface IReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByGalleryId(Long gallaryId); // Find reviews by Gallery ID
}
