package com.michaelnguyen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.michaelnguyen.dto.request.GalleryRequest;
import com.michaelnguyen.dto.response.GalleryResponse;
import com.michaelnguyen.entity.Gallery;

@Mapper(componentModel = "spring")
public interface IGalleryMapper {

	@Mapping(target = "tag", ignore = true)
	Gallery toGallery(GalleryRequest request);

	@Mapping(source = "user.id", target = "user_id")
	@Mapping(source = "tag.name", target = "nameTag")
	GalleryResponse toGalleryResponse(Gallery gallery);

}
