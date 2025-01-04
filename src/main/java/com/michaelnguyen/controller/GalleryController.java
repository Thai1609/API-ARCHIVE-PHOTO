package com.michaelnguyen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.michaelnguyen.dto.request.GalleryRequest;
import com.michaelnguyen.dto.response.ApiResponse;
import com.michaelnguyen.dto.response.GalleryResponse;
import com.michaelnguyen.entity.Gallery;
import com.michaelnguyen.service.GalleryService;

@RestController
@RequestMapping("api/gallery")
public class GalleryController {

	@Autowired
	private GalleryService galleryService;

	@PostMapping(value = "/upload-image", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public GalleryResponse createProduct(@RequestPart("file") List<MultipartFile> multipartFile,
			@RequestPart GalleryRequest request) {

		return galleryService.uploadImage(multipartFile, request);
	}

	@GetMapping("/{id}")
	ApiResponse<?> getGalleryById(@PathVariable Long id) {
		return ApiResponse.builder().result(galleryService.getGalleryById(id)).build();
	}

	@GetMapping("/{id}/related")
	ApiResponse<?> getRelatedGalleries(@PathVariable Long id, @RequestParam(value = "userId") Long userId) {
		return ApiResponse.builder().result(galleryService.getRelatedGalleries(id, userId)).build();
	}

	@DeleteMapping("/{id}")
	public void deleteImage(@PathVariable Long id) {
		galleryService.deleteImage(id);
	}

	@GetMapping("/get-all")
	public Page<GalleryResponse> getAllGalleries(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		return galleryService.getAllGalleries(page, size);
	}

	@GetMapping("/my-gallery")
	public Page<GalleryResponse> getAllGalleriesByUser(@RequestParam Long userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
		return galleryService.getAllGalleriesByUser(userId, page, size);
	}
}