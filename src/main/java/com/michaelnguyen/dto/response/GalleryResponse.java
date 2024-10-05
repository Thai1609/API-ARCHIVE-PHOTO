package com.michaelnguyen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalleryResponse {
 
	private String nameImage;

	private String urlImage;
	
	private String status;
}
