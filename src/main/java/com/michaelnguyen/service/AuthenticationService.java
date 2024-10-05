package com.michaelnguyen.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.michaelnguyen.dto.request.AuthenticationRequest;
import com.michaelnguyen.dto.request.IntrospectRequest;
import com.michaelnguyen.dto.response.AuthenticationResponse;
import com.michaelnguyen.dto.response.IntrospectResponse;
import com.michaelnguyen.entity.User;
import com.michaelnguyen.error.AppException;
import com.michaelnguyen.error.ErrorCode;
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

	public AuthenticationResponse authenticate(AuthenticationRequest request) {

		if (!iUserRepository.existsByEmail(request.getEmail()))
			throw new AppException(ErrorCode.USER_NOT_EXIST);

		User user = iUserRepository.findByEmail(request.getEmail());

		if (!user.isEnabled())
			throw new AppException(ErrorCode.USER_NOT_ACTIVATED);
		
		boolean authentication = passwordEncoder.matches(request.getPassword(), user.getPassword());
		if (!authentication)
			throw new AppException(ErrorCode.UNAUTHENTICATED);

		var token = generateToken(user);

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
