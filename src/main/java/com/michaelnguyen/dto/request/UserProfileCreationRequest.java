package com.michaelnguyen.dto.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileCreationRequest {
	private String firstName;

	private String lastName;

	private String address;

	private String gender;

	private String phone;

	private Date dob;

	private Date created_at;

}
