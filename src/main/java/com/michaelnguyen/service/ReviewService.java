package com.michaelnguyen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.michaelnguyen.entity.Gallery;
import com.michaelnguyen.entity.Review;
import com.michaelnguyen.entity.User;
import com.michaelnguyen.repository.IUserRepository;
import com.michaelnguyen.repository.IGalleryRepository;
import com.michaelnguyen.repository.IReviewRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

	@Autowired
	private IReviewRepository iReviewRepository;
	@Autowired
	private IGalleryRepository iGalleryRepository;

	@Autowired
	private IUserRepository iUserRepository;

	public List<Review> getReviewsForGallery(Long galleryId) {
		return iReviewRepository.findByGalleryId(galleryId);
	}

	public Review addReview(Long galleryId, Long userId, int rating, String comment) {
		
		Gallery gallery = iGalleryRepository.findById(galleryId).orElseThrow();
		
		User user = iUserRepository.findById(userId).orElseThrow();

		Review review = new Review();
		review.setGallery(gallery);
		review.setUser(user);
		review.setRating(rating);
		review.setComment(comment);
		review.setCreatedDate(LocalDateTime.now());

		return iReviewRepository.save(review);
	}
}
