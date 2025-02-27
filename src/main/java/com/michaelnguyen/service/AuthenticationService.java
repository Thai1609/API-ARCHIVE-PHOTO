package com.michaelnguyen.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.michaelnguyen.dto.request.UserCreationRequest;
import com.google.firebase.auth.FirebaseAuthException;
import com.michaelnguyen.dto.request.IntrospectRequest;
import com.michaelnguyen.dto.request.LoginRequest;
import com.michaelnguyen.dto.response.AuthenticationResponse;
import com.michaelnguyen.dto.response.IntrospectResponse;
import com.michaelnguyen.entity.User;
import com.michaelnguyen.error.AppException;
import com.michaelnguyen.error.ErrorCode;
import com.michaelnguyen.repository.IRoleRepository;
import com.michaelnguyen.repository.IUserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.experimental.NonFinal;

@Service
public class AuthenticationService {
	@Autowired
	IUserRepository iUserRepository;

	@Autowired
	IRoleRepository iRoleRepository;

	@Autowired
	private UserProfileService userProfileService;
	
    @Autowired
	private PasswordEncoder passwordEncoder;

	@NonFinal
	@Value("${jwt.signerKey}")
	protected String SIGNER_KEY;

	public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
		var token = request.getToken();

		JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

		SignedJWT signedJWT = SignedJWT.parse(token);

		Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

		var verified = signedJWT.verify(verifier);

		return IntrospectResponse.builder().valid(verified && expiryTime.after(new Date())).build();

	}

	// Login with email and password
	public AuthenticationResponse authenticateWithEmail(LoginRequest request) {
		Optional<User> user = iUserRepository.findByOptions(request.getEmail(), null, null);
		if (user.isPresent() == false)
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		
		boolean authentication = passwordEncoder.matches(request.getPassword(), user.get().getPassword());
		if (!authentication)
			throw new AppException(ErrorCode.UNAUTHENTICATED);

		var token = generateToken(user.get());

		return AuthenticationResponse.builder().token(token).authenticated(true).build();

	}

	// Login with Provider
	public AuthenticationResponse authenticateWithProvider(UserCreationRequest request) throws FirebaseAuthException {
	
		Optional<User> userCheck = iUserRepository.findByOptions(request.getEmail(), request.getProvider(),
				request.getProviderId());

		if (userCheck.isPresent()) {
			var token = generateToken(userCheck.get());
			return AuthenticationResponse.builder().token(token).authenticated(true).build();
		}

		// Register new user if not found
		User newUser = new User();
		newUser.setEmail(request.getEmail());
		newUser.setProvider(request.getProvider());
		newUser.setProviderId(request.getProviderId());

		var roles = iRoleRepository.findAllById(Arrays.asList("USER"));
		newUser.setRoles(new HashSet<>(roles));

		newUser = iUserRepository.save(newUser);

		Optional<User> user = iUserRepository.findByOptions(request.getEmail(), request.getProvider(),
				request.getProviderId());

		userProfileService.createUserProfile(user.get().getId(), request);

		var token = generateToken(user.get());

		return AuthenticationResponse.builder().token(token).authenticated(true).build();
		
	}

//	public AuthenticationResponse refreshToken(AuthenticationRequest request) {
//
//		if (!iUserRepository.existsByEmail(request.getEmail()))
//			throw new AppException(ErrorCode.USER_NOT_EXIST);
//
//		User user = iUserRepository.findByEmail(request.getEmail()).get();
//
//		boolean authentication = passwordEncoder.matches(request.getPassword(), user.getPassword());
//
//		if (!authentication)
//			throw new AppException(ErrorCode.UNAUTHENTICATED);
//
//		var token = generateToken(user);
//
//		return AuthenticationResponse.builder().token(token).authenticated(true).build();
//	}

	private String generateToken(User user) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject(user.getEmail()).issuer("michael")
				.issueTime(new Date()).expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
				.claim("scope", buildRole(user)).build();

		Payload payload = new Payload(jwtClaimsSet.toJSONObject());

		JWSObject jwsObject = new JWSObject(header, payload);

		try {
			jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
			return jwsObject.serialize();
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		return null;

	}

	private String buildRole(User user) {
		StringJoiner sj = new StringJoiner(" ");
		if (!CollectionUtils.isEmpty(user.getRoles())) {

			user.getRoles().forEach(roles -> {
				sj.add("ROLE_" + roles.getName());
				if (!CollectionUtils.isEmpty(roles.getPermissions())) {
					roles.getPermissions().forEach(permission -> sj.add(permission.getName()));
				}

			});
		}
		return sj.toString();

	}

}
