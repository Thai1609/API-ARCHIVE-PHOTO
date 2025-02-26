package com.michaelnguyen.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.michaelnguyen.dto.response.ProductResponse;
import com.michaelnguyen.entity.Product;
import com.michaelnguyen.entity.ProductImage;
import com.michaelnguyen.entity.ProductTag;

@Mapper(componentModel = "spring")
public interface IProductMapper {
	IProductMapper INSTANCE = Mappers.getMapper(IProductMapper.class);

	@Mapping(source = "category.name", target = "category")
	@Mapping(source = "brand.name", target = "brand")
	@Mapping(source = "user.email", target = "seller")
	@Mapping(source = "images", target = "images", qualifiedByName = "mapImages")
	@Mapping(source = "variants", target = "variants")
	@Mapping(source = "reviews", target = "reviews")
	@Mapping(source = "tags", target = "tags", qualifiedByName = "mapTags")
	@Mapping(source = "status", target = "status", qualifiedByName = "mapStatus")
	ProductResponse toProductResponse(Product product);

	// Convert `Set<ProductImage>` to `Set<String>` (image URLs)
	@Named("mapImages")
	default Set<String> mapImages(Set<ProductImage> imageUrl) {
		if (imageUrl == null)
			return null;
		return imageUrl.stream().map(ProductImage::getImageUrl)  
				.collect(Collectors.toSet());
	}

	@Named("mapTags")
	default Set<String> mapTags(Set<ProductTag> tags) {
		if (tags == null)
			return null;
		return tags.stream().map(tag -> tag.getTag().getName())  
				.collect(Collectors.toSet());
	}

	// Convert `ProductStatus` ENUM to String
	@Named("mapStatus")
	default String mapStatus(Enum<?> status) {
		return status != null ? status.name() : "UNKNOWN";
	}
}
