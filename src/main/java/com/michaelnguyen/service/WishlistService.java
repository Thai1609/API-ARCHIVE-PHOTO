package com.michaelnguyen.service;

import com.michaelnguyen.entity.User;
import com.michaelnguyen.entity.Wishlist;
import com.michaelnguyen.repository.IWishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final IWishlistRepository iWishlistRepository;

    public WishlistService(IWishlistRepository iWishlistRepository) {
        this.iWishlistRepository = iWishlistRepository;
    }

    // Get wishlist items by user
    public List<Wishlist> getWishlist(User user) {
        return iWishlistRepository.findByUser(user);
    }

    // Add a product to wishlist
    public Wishlist addToWishlist(User user, String galleryId) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setGalleryId(galleryId);
        return iWishlistRepository.save(wishlist);
    }

    @Transactional // Remove a product from wishlist
    public void removeFromWishlist(User user, String galleryId) {
        iWishlistRepository.deleteByUserAndGalleryId(user, galleryId);
    }
}
