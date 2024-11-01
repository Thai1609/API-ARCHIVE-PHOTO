package com.michaelnguyen.service;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.michaelnguyen.entity.User;
import com.michaelnguyen.error.AppException;
import com.michaelnguyen.error.ErrorCode;
import com.michaelnguyen.repository.IRoleRepository;
import com.michaelnguyen.repository.IUserRepository;

@Service
public class CustomOidcUserService extends OidcUserService {

	@Autowired
	IUserRepository iUserRepository;

	@Autowired
	private IRoleRepository iRoleRepository;

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private AuthenticationService authenticationService;

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) {
		// Load the default OIDC user
		OidcUser oidcUser = super.loadUser(userRequest);
//		String email = oidcUser.getEmail();
//
//		if (iUserRepository.existsByEmail(email))
//			throw new AppException(ErrorCode.EMAIL_EXIST);
//
//		// Create a new user if not present
//		User user = new User();
//		user.setEmail(email);
//		user.setGoogleId(oidcUser.getSubject()); // Use the Google sub (subject) as the ID
//
//		var roles = iRoleRepository.findAllById(Arrays.asList("USER"));
//		user.setRoles(new HashSet<>(roles));
//
//		iUserRepository.save(user); // Save the user to the database
//
//		User user1 = iUserRepository.findByEmail(email);
//		userProfileService.createUserProfile(user1.getId());// Save the user profile to the database
//
//		
//		authenticationService.authenticateLoginGoogle(email);
		
		return oidcUser;
	}
}
