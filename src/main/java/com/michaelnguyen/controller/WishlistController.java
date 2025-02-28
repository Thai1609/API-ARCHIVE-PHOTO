package com.michaelnguyen.controller;

import com.michaelnguyen.entity.User;
import com.michaelnguyen.entity.Wishlist;
import com.michaelnguyen.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

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
