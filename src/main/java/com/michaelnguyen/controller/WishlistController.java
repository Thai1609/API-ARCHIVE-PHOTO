package com.michaelnguyen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.michaelnguyen.entity.User;
import com.michaelnguyen.entity.Wishlist;
import com.michaelnguyen.service.WishlistService;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
	@Autowired
	private WishlistService wishlistService;

	// Get wishlist for a user
	@GetMapping
	public ResponseEntity<List<Wishlist>> getWishlist(@RequestParam(value = "userId") String userId) {
		User user = new User();
		user.setId(Long.parseLong(userId)); // Assuming User ID is sent as a parameter
		return ResponseEntity.ok(wishlistService.getWishlist(user));
	}

	// Add to wishlist
	@PostMapping
	public ResponseEntity<Wishlist> addToWishlist(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "galleryId") String galleryId) {
		User user = new User();
		user.setId(Long.parseLong(userId));
		return ResponseEntity.ok(wishlistService.addToWishlist(user, galleryId));
	}

	// Remove from wishlist
	@DeleteMapping
	public ResponseEntity<String> removeFromWishlist(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "galleryId") String galleryId) {
		User user = new User();
		user.setId(Long.parseLong(userId));
		wishlistService.removeFromWishlist(user, galleryId);
		return ResponseEntity.ok("Removed from wishlist");
	}
}
