package com.michaelnguyen.mapper;

import com.michaelnguyen.dto.response.ProductResponse;
import com.michaelnguyen.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IProductMapper {
    @Mapping(source = "category.name", target = "category")
    @Mapping(source = "brand.name", target = "brand")
    ProductResponse toProductResponse(Product product, ProductImage productImage, ProductReview productReview, ProductVariant productVariant, ProductTag productTag);

}
