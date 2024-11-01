package com.michaelnguyen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
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
import com.michaelnguyen.repository.IUserProfileRepository;
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
			gallery.setTag(tag);
			gallery.setUser(user);

			gallery = iGalleryRepository.save(gallery);
		}

		return iGalleryMapper.toGalleryResponse(gallery);
	}

	public List<GalleryResponse> getAllImage() {
		return iGalleryRepository.findAll().stream().map(iGalleryMapper::toGalleryResponse).toList();
	}

	public GalleryResponse getImageById(Long id) {
		Gallery gallery = iGalleryRepository.findById(id).orElseThrow();

		return iGalleryMapper.toGalleryResponse(gallery);
	}

	public void deleteImage(Long id) {
		iGalleryRepository.deleteById(id);
	}

	public Page<Gallery> getAllGalleries(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return iGalleryRepository.findAll(pageable);
	}

	public Page<Gallery> getAllGalleriesById(Long userId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return iGalleryRepository.findAllGalleryById(userId, pageable);
	}

}
