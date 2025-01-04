package com.michaelnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.michaelnguyen.dto.request.TagRequest;
import com.michaelnguyen.dto.response.ApiResponse;
import com.michaelnguyen.dto.response.TagResponse;
import com.michaelnguyen.service.TagService;

@RestController
@RequestMapping("api/gallery/tag")
public class TagController {

	@Autowired
	private TagService tagService;

	@PostMapping("/create")
	public TagResponse createTag(@RequestBody TagRequest request) {
		return tagService.createTag(request);
	}

	@GetMapping("/get-all")
	ApiResponse<?> getAllTags(@RequestParam(value = "userId") Long userId) {
		return ApiResponse.builder().result(tagService.getAllTag(userId)).build();
	}

	@DeleteMapping("/delete")
	public void deleteTypeImage(@RequestParam(value = "nameTag") String name) {
		tagService.deleteTag(name);
	}

}