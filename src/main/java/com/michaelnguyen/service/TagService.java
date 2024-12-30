package com.michaelnguyen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.michaelnguyen.dto.request.TagRequest;
import com.michaelnguyen.dto.response.TagResponse;
import com.michaelnguyen.entity.Tag;
import com.michaelnguyen.entity.User;
import com.michaelnguyen.mapper.ITagMapper;
import com.michaelnguyen.repository.ITagRepository;
import com.michaelnguyen.repository.IUserRepository;

@Service
public class TagService {

	@Autowired
	ITagRepository iTagRepository;
	@Autowired
	ITagMapper iTagMapper;
	@Autowired
	IUserRepository iUserRepository;

	public TagResponse createTag(TagRequest request) {
		Tag type = iTagMapper.toTag(request);
		User user = iUserRepository.findById(request.getUserId()).get();
		
		type.setUser(user);
		type = iTagRepository.save(type);

		return iTagMapper.toTagResponse(type);
	}

	public List<TagResponse> getAllTag(Long userId ) {
		var tags = iTagRepository.findByOptions(userId, Sort.by(Sort.Order.asc("name")));
		return tags.stream().map(iTagMapper::toTagResponse).toList();
	}

	public void deleteTag(int id) {
		iTagRepository.deleteById(id);
	}
}
