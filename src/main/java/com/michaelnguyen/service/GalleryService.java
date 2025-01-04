package com.michaelnguyen.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.michaelnguyen.dto.request.GalleryRequest;
import com.michaelnguyen.dto.response.GalleryResponse;
import com.michaelnguyen.entity.Gallery;
import com.michaelnguyen.entity.Tag;
import com.michaelnguyen.entity.User;
import com.michaelnguyen.firebase.UploadService;
import com.michaelnguyen.mapper.IGalleryMapper;
import com.michaelnguyen.mapper.ITagMapper;
import com.michaelnguyen.repository.IGalleryRepository;
import com.michaelnguyen.repository.ITagRepository;
import com.michaelnguyen.repository.IUserRepository;

@Service
public class GalleryService {

	@Autowired
	IGalleryRepository iGalleryRepository;

	@Autowired
	IUserRepository iUserRepository;

	@Autowired
	ITagRepository iTagRepository;
	@Autowired
	IGalleryMapper iGalleryMapper;

	@Autowired
	ITagMapper iTagMapper;

	@Autowired
	private UploadService uploadService;

	public GalleryResponse uploadImage(List<MultipartFile> multipartFile, GalleryRequest request) {
		var gallery = iGalleryMapper.toGallery(request);

		Tag tag = iTagRepository.findTagByName(request.getNameTag()).orElseThrow();
		User user = iUserRepository.findById(request.getUserId()).orElseThrow();

		for (int i = 0; i < multipartFile.size(); i++) {

			gallery.setId(iGalleryRepository.newIdGallery());
			gallery.setNameImage(multipartFile.get(i).getOriginalFilename());
			gallery.setUrlImage(uploadService.upload(multipartFile.get(i), "galleries/" + tag.getName() + "/"));
			gallery.setDescription(request.getDescription());
			gallery.setStatus(request.getStatus());
			gallery.setTag(tag);
			gallery.setUser(user);

			gallery = iGalleryRepository.save(gallery);
		}

		return iGalleryMapper.toGalleryResponse(gallery);
	}

	public Optional<Gallery> getGalleryById(Long id) {
		return iGalleryRepository.findById(id);
	}

	public List<GalleryResponse> getRelatedGalleries(Long id, Long userId) {

		Optional<Gallery> optionalGallery = iGalleryRepository.findById(id);
		if (optionalGallery.isEmpty())
			return Collections.emptyList();
		Gallery gallery = optionalGallery.get();

		List<Gallery> relatedGalleries = iGalleryRepository.getGalleryByTag(gallery.getTag().getName(), userId);
		relatedGalleries = relatedGalleries.stream().collect(Collectors.toList());

		return relatedGalleries.stream().map(iGalleryMapper::toGalleryResponse).toList();
	}

	public void deleteImage(Long id) {
		iGalleryRepository.deleteById(id);
	}

	public Page<GalleryResponse> getAllGalleries(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return iGalleryRepository.findAllGalleries(pageable).map(iGalleryMapper::toGalleryResponse);
	}

	public Page<GalleryResponse> getAllGalleriesByUser(Long userId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return iGalleryRepository.findAllGalleriesByUser(userId, pageable).map(iGalleryMapper::toGalleryResponse);
	}

}
