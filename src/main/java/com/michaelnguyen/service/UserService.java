package com.michaelnguyen.service;

<<<<<<< HEAD
=======
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuthException;
>>>>>>> origin/main
import com.michaelnguyen.dto.request.UserCreationRequest;
import com.michaelnguyen.dto.request.UserUpdateRequest;
import com.michaelnguyen.dto.response.UserResponse;
import com.michaelnguyen.entity.User;
import com.michaelnguyen.entity.VerificationToken;
import com.michaelnguyen.error.AppException;
import com.michaelnguyen.error.ErrorCode;
import com.michaelnguyen.mapper.IUserMapper;
import com.michaelnguyen.repository.IRoleRepository;
import com.michaelnguyen.repository.IUserRepository;
import com.michaelnguyen.repository.IVerificationTokenRepository;
import com.michaelnguyen.repository.UserRepositoryCustom;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final IUserRepository iUserRepository;

    private final UserRepositoryCustom userRepositoryCustom;

    private final IRoleRepository iRoleRepository;

    private final PasswordEncoder passwordEncoder;

    private final IVerificationTokenRepository iVerificationTokenRepository;

    private final IUserMapper iUserMapper;

    private final UserProfileService userProfileService;

    public UserService(IUserRepository iUserRepository,
                       UserRepositoryCustom userRepositoryCustom,
                       IRoleRepository iRoleRepository,
                       PasswordEncoder passwordEncoder,
                       IVerificationTokenRepository iVerificationTokenRepository,
                       IUserMapper iUserMapper,
                       UserProfileService userProfileService) {
        this.iUserRepository = iUserRepository;
        this.userRepositoryCustom = userRepositoryCustom;
        this.iRoleRepository = iRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.iVerificationTokenRepository = iVerificationTokenRepository;
        this.iUserMapper = iUserMapper;
        this.userProfileService = userProfileService;
    }


    public UserResponse createUser(UserCreationRequest request) {
        Optional<User> user = iUserRepository.findByOptions(request.getEmail(), null, null);

        if (user.isPresent()) throw new AppException(ErrorCode.EMAIL_EXIST);

        // Create a new user
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save the new user
        var roles = iRoleRepository.findAllById(List.of("USER"));
        newUser.setRoles(new HashSet<>(roles));

        newUser = iUserRepository.save(newUser);

        Optional<User> userSave = iUserRepository.findByOptions(request.getEmail(), null, null);

        userProfileService.createUserProfile(userSave.get().getId(), request);

        return iUserMapper.toUserResponse(newUser);

    }

    @PostAuthorize("returnObject.email==authentication.name")
    public UserResponse updateUserByUser(Long id, UserUpdateRequest request) {
        User user = iUserRepository.findById(id).orElseThrow();

        String sql = "";
        sql += "update users u set ";

        if (!request.getPassword().isEmpty()) {
            sql += " u.password = '" + passwordEncoder.encode(request.getPassword()) + "'";
        }

        sql += " where u.id = '" + id + "'";
        userRepositoryCustom.updateUserById(sql);

        return iUserMapper.toUserResponse(user);

    }

    @PostAuthorize("returnObject.email==authentication.name")
    public UserResponse getInfo(UserCreationRequest request) {

        Optional<User> user = iUserRepository.findByOptions(request.getEmail(), request.getProvider() == null ? null : request.getProvider().toUpperCase(), request.getProviderId());

        return iUserMapper.toUserResponse(user.get());
    }

    public List<UserResponse> getAllUser() {
        return iUserRepository.findAll().stream().map(iUserMapper::toUserResponse).toList();
    }

    public UserResponse updateUserByAdmin(Long id, UserUpdateRequest request) {
        User user = iUserRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        String sql = "";
        sql += "update users u set ";

        if (!request.getPassword().isEmpty()) {
            sql += " u.password = '" + passwordEncoder.encode(request.getPassword()) + "'";
        }
        if (request.isEnabled()) {
            sql += " u.enabled = '1'";
        } else {
            sql += " u.enabled = '0'";

        }

        sql += " where u.id = '" + id + "'";
        userRepositoryCustom.updateUserById(sql);

        return iUserMapper.toUserResponse(user);

    }

    public UserResponse updateRolesUser(Long id, String role_names) {

        User user = iUserRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        String sql_delete = "delete from users_roles u where u.User_id = '" + id + "'";
        userRepositoryCustom.updateUserById(sql_delete);

        String sql_update = "";
        String[] arrRoles = role_names.split(",");
        for (String role : arrRoles) {
            sql_update = "INSERT INTO users_roles(User_id,roles_name) VALUES (" + id + ",'" + role + "')";
            userRepositoryCustom.updateUserById(sql_update);
        }

        return iUserMapper.toUserResponse(user);

    }

    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user, LocalDateTime.now().plusHours(24));
        try {
            iVerificationTokenRepository.save(verificationToken);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return token;

    }

}
