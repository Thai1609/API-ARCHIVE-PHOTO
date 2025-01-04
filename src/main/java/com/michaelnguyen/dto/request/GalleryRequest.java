package com.michaelnguyen.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalleryRequest {
	private String nameTag;
	private String description;
	private String status;
	private Long userId;
}
