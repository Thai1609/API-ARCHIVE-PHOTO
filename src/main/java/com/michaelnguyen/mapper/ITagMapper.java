package com.michaelnguyen.mapper;

import org.mapstruct.Mapper;

import com.michaelnguyen.dto.request.TagRequest;
import com.michaelnguyen.dto.response.TagResponse;
import com.michaelnguyen.entity.Tag;

@Mapper(componentModel = "spring")
public interface ITagMapper {

	Tag toTag(TagRequest request);

	TagResponse toTagResponse(Tag tag);

}
 