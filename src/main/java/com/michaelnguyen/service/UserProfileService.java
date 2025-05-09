package com.michaelnguyen.service;

import com.michaelnguyen.dto.request.UserCreationRequest;
import com.michaelnguyen.entity.UserProfile;
import com.michaelnguyen.repository.IUserProfileRepository;
import com.michaelnguyen.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UserProfileService {

    private final IUserProfileRepository iUserProfileRepository;


    private final IUserRepository iUserRepository;

    public UserProfileService(IUserProfileRepository iUserProfileRepository,
            IUserRepository iUserRepository) {
        this.iUserProfileRepository = iUserProfileRepository;
        this.iUserRepository = iUserRepository;
    }

    public List<UserProfile> getAllUser() throws IOException {
        return iUserProfileRepository.findAll();

    }

    public void createUserProfile(Long id, UserCreationRequest request) {
        UserProfile userProfile = new UserProfile();

        userProfile.setFullName(request.getName());
        userProfile.setAvatarUrl(request.getImageUrl());
        userProfile.setUser(iUserRepository.findById(id).orElseThrow());

        iUserProfileRepository.save(userProfile);

    }


}
