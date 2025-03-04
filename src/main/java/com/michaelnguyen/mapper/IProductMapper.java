package com.michaelnguyen.mapper;

import com.michaelnguyen.dto.response.ProductResponse;
import com.michaelnguyen.entity.Product;
import com.michaelnguyen.entity.ProductTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

<<<<<<< HEAD
=======
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.michaelnguyen.dto.response.ProductResponse;
import com.michaelnguyen.entity.Product;
import com.michaelnguyen.entity.ProductTag;

>>>>>>> origin/main
@Mapper(componentModel = "spring")
public interface IProductMapper {
    @Mapping(source = "category.name", target = "category")
    @Mapping(source = "brand.name", target = "brand")
    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTags")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatus")
    ProductResponse toProductResponse(Product product);

    @Named("mapTags")
    default Set<String> mapTags(Set<ProductTag> tags) {
        if (tags == null)
            return null;
        return tags.stream().map(tag -> tag.getTag().getName()).collect(Collectors.toSet());
    }

    // Convert `ProductStatus` ENUM to String
    @Named("mapStatus")
    default String mapStatus(Enum<?> status) {
        return status != null ? status.name() : "UNKNOWN";
    }
}
