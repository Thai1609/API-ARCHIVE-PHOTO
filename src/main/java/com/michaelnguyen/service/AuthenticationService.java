package com.michaelnguyen.service;

import com.michaelnguyen.dto.request.IntrospectRequest;
import com.michaelnguyen.dto.request.UserCreationRequest;
import com.michaelnguyen.dto.response.AuthenticationResponse;
import com.michaelnguyen.dto.response.IntrospectResponse;
import com.michaelnguyen.entity.User;
import com.michaelnguyen.error.AppException;
import com.michaelnguyen.error.ErrorCode;
import com.michaelnguyen.repository.IRoleRepository;
import com.michaelnguyen.repository.IUserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service

public class AuthenticationService {

    private final IUserRepository iUserRepository;
    private final IRoleRepository iRoleRepository;
    private final UserProfileService userProfileService;
    private final PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationService(IUserRepository iUserRepository, IRoleRepository iRoleRepository, UserProfileService userProfileService, PasswordEncoder passwordEncoder) {
        this.iUserRepository = iUserRepository;
        this.iRoleRepository = iRoleRepository;
        this.userProfileService = userProfileService;
        this.passwordEncoder = passwordEncoder;
    }


    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder().valid(verified && expiryTime.after(new Date())).build();

    }

    // Login with email and password
    public AuthenticationResponse authenticateWithEmail(UserCreationRequest request) {
        Optional<User> user = iUserRepository.findByOptions(request.getEmail(), null, null);
        if (user.isEmpty()) throw new AppException(ErrorCode.UNAUTHENTICATED);

        boolean authentication = passwordEncoder.matches(request.getPassword(), user.get().getPassword());
        if (!authentication) throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user.get());

        return AuthenticationResponse.builder().token(token).authenticated(true).build();

    }

    // Login with Provider
    public AuthenticationResponse authenticateWithProvider(UserCreationRequest request) {

        Optional<User> userCheck = iUserRepository.findByOptions(request.getEmail(), request.getProvider(), request.getProviderId());

        if (userCheck.isPresent()) {
            var token = generateToken(userCheck.get());
            return AuthenticationResponse.builder().token(token).authenticated(true).build();
        }

        // Register new user if not found
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setProvider(request.getProvider());
        newUser.setProviderId(request.getProviderId());

        var roles = iRoleRepository.findAllById(List.of("USER"));
        newUser.setRoles(new HashSet<>(roles));

        newUser = iUserRepository.save(newUser);

        Optional<User> user = iUserRepository.findByOptions(request.getEmail(), request.getProvider(), request.getProviderId());

        userProfileService.createUserProfile(user.get().getId(), request);

        var token = generateToken(user.get());

        return AuthenticationResponse.builder().token(token).authenticated(true).build();

    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject(user.getEmail()).issuer("michael").issueTime(new Date()).expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())).claim("scope", buildRole(user)).build();

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
        if (user == null || CollectionUtils.isEmpty(user.getRoles())) {
            return ""; // Nếu User hoặc Roles trống, trả về chuỗi rỗng
        }

        // Sử dụng StringJoiner để xây dựng chuỗi Roles và Permissions
        StringJoiner roleAndPermissions = new StringJoiner(" ");

        user.getRoles().forEach(role -> {
            // Thêm Role vào chuỗi (Role được tiền tố với "ROLE_")
            roleAndPermissions.add("ROLE_" + role.getName().toUpperCase());

            // Nếu Role có Permission, thêm các Permission vào chuỗi
            if (!CollectionUtils.isEmpty(role.getPermissions())) {
                role.getPermissions().forEach(permission ->
                        roleAndPermissions.add(permission.name())); // Sử dụng name() của Enum Permission
            }
        });

        return roleAndPermissions.toString();
    }

}
