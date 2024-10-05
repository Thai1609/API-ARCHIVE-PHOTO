package com.michaelnguyen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.michaelnguyen.dto.request.GalleryRequest;
import com.michaelnguyen.dto.response.GalleryResponse;
import com.michaelnguyen.entity.Tag;
import com.michaelnguyen.entity.Gallery;
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

	public GalleryResponse uploadImage(List<MultipartFile> multipartFile, GalleryRequest request) {
		var gallery = iGalleryMapper.toGallery(request);

		Tag tag = iTagRepository.findById(request.getTag()).orElseThrow();

		for (int i = 0; i < multipartFile.size(); i++) {

 
			gallery.setId(iGalleryRepository.newIdGallery());
			gallery.setNameImage(multipartFile.get(i).getOriginalFilename());
			gallery.setUrlImage(uploadService.upload(multipartFile.get(i), "galleries/" + tag.getName() + "/"));
			gallery.setTag(tag);

			gallery = iGalleryRepository.save(gallery);
		}

		return iGalleryMapper.toGalleryResponse(gallery);
	}

	public List<GalleryResponse> getAllImage() {
		return iGalleryRepository.findAll().stream().map(iGalleryMapper::toGalleryResponse).toList();
	}

	public void deleteImage(Integer id) {
		iGalleryRepository.deleteById(id);
	}

}
