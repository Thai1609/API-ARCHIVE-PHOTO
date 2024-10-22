package com.michaelnguyen.service;

import java.util.List;

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

@Service
public class GalleryService {

	@Autowired
	IGalleryRepository iGalleryRepository;
	@Autowired
	ITagRepository iTagRepository;
	@Autowired
	IGalleryMapper iGalleryMapper;

	@Autowired
	ITagMapper iTagMapper;

	@Autowired
	private UploadService uploadService;

	public GalleryResponse uploadImage(MultipartFile[] multipartFile, GalleryRequest request) {
		var gallery = iGalleryMapper.toGallery(request);

		Tag tag = iTagRepository.findTagByName(request.getTag()).orElseThrow();
		for (MultipartFile file : multipartFile) {

			gallery.setId(iGalleryRepository.newIdGallery());
			gallery.setNameImage(request.getNameImage());
			gallery.setUrlImage(uploadService.upload(file, "galleries/" + tag.getName() + "/"));
			gallery.setTag(tag);

			gallery = iGalleryRepository.save(gallery);
		}

		return iGalleryMapper.toGalleryResponse(gallery);
	}

	public List<GalleryResponse> getAllImage() {
		return iGalleryRepository.findAll().stream().map(iGalleryMapper::toGalleryResponse).toList();
	}

	public GalleryResponse getImageById(Integer id) {
		Gallery gallery = iGalleryRepository.findById(id).orElseThrow();
		return iGalleryMapper.toGalleryResponse(gallery);
	}

	public void deleteImage(Integer id) {
		iGalleryRepository.deleteById(id);
	}

	public Page<Gallery> getGalleries(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return iGalleryRepository.findAll(pageable);
	}

}
