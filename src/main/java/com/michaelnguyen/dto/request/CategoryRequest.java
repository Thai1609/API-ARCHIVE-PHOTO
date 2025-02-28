package com.michaelnguyen.dto.request;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private Long parentId;
}
