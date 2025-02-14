package com.michaelnguyen.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {

	private String fullName;

	private String address;

	private String phone;

	private String gender;

	private byte[] image;

	private Date dob;

	private Date created_at;

	private Date modified_at;
}
