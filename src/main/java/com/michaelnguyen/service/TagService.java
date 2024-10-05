package com.michaelnguyen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.michaelnguyen.dto.request.TagRequest;
import com.michaelnguyen.dto.response.TagResponse;
import com.michaelnguyen.entity.Tag;
import com.michaelnguyen.mapper.ITagMapper;
import com.michaelnguyen.repository.ITagRepository;

@Service
public class TagService {

	@Autowired
	ITagRepository iTagRepository;
	@Autowired
	ITagMapper iTagMapper;

	public TagResponse createTag(TagRequest request) {
		Tag type = iTagMapper.toTag(request);
		type = iTagRepository.save(type);

		return iTagMapper.toTagResponse(type);
	}

	public List<TagResponse> getAllTag() {
		var tags = iTagRepository.findAll();
		return tags.stream().map(iTagMapper::toTagResponse).toList();
	}

	public void deleteTag(int id) {
		iTagRepository.deleteById(id);
	}
}
