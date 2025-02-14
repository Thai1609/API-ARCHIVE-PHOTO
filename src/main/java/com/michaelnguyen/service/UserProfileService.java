package com.michaelnguyen.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.michaelnguyen.dto.request.UserCreationRequest;
import com.michaelnguyen.dto.request.UserProfileUpdateRequest;
import com.michaelnguyen.dto.response.UserProfileResponse;
import com.michaelnguyen.entity.User;
import com.michaelnguyen.entity.UserProfile;
import com.michaelnguyen.error.AppException;
import com.michaelnguyen.error.ErrorCode;
import com.michaelnguyen.mapper.IUserProfileMapper;
import com.michaelnguyen.repository.IUserProfileRepository;
import com.michaelnguyen.repository.IUserRepository;

@Service
public class UserProfileService {
	@Autowired
	private IUserProfileRepository iUserProfileRepository;
	@Autowired
	private IUserProfileMapper iUserProfileMapper;

	@Autowired
	private IUserRepository iUserRepository;

	public List<UserProfile> getAllUser() throws IOException {
		return iUserProfileRepository.findAll();

	}

	public UserProfileResponse getUserProfileById(Long id) {
		return iUserProfileMapper.toUserProfileResponse(
				iUserProfileRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)));

	}

//	@PostAuthorize("returnObject.email==authentication.name")
	public UserProfileResponse createUserProfile(Long id, UserCreationRequest request) {

		UserProfile userProfile = new UserProfile();
		userProfile.setFullName(request.getName());
		userProfile.setAvatarUrl(request.getImageUrl());
		userProfile.setUser(iUserRepository.findById(id).orElseThrow());

		return iUserProfileMapper.toUserProfileResponse(iUserProfileRepository.save(userProfile));

	}

	public UserProfileResponse updateUserProfile(Long id, UserProfileUpdateRequest request) throws IOException {
		User user = iUserRepository.findById(id).orElseThrow();

		UserProfile userProfile = iUserProfileRepository.findById(user.getUserProfile().getId())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

// 		SimpleDateFormat sdfDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
//
//		String formattedDate = sdf.format(request.getDob());

		request.setDob(request.getDob());

		request.setModified_at(new Date());

		iUserProfileMapper.updateUserProfile(userProfile, request);

		return iUserProfileMapper.toUserProfileResponse(iUserProfileRepository.save(userProfile));

	}

}
