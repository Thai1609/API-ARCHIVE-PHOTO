package com.michaelnguyen.mapper;

import com.michaelnguyen.dto.response.ProductResponse;
import com.michaelnguyen.entity.Product;
import com.michaelnguyen.entity.ProductTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface IProductMapper {
    @Mapping(source = "brand.name", target = "brand")
    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTags")
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

    List<ProductResponse> toProductResponse(List<Product> products);

}
