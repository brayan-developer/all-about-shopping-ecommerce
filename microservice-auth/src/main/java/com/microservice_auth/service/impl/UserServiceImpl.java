package com.microservice_auth.service.impl;

import com.microservice_auth.dto.UserDto;
import com.microservice_auth.entity.Role;
import com.microservice_auth.entity.User;
import com.microservice_auth.enums.AccountStatus;
import com.microservice_auth.enums.RoleName;
import com.microservice_auth.event.producer.EmailVerificationProducer;
import com.microservice_auth.exceptions.EmailAlreadyRegisteredException;
import com.microservice_auth.exceptions.ResourceNotFoundException;
import com.microservice_auth.mappers.UserMapper;
import com.microservice_auth.repository.IRoleRepository;
import com.microservice_auth.repository.IUserRepository;
import com.microservice_auth.request.CreateUserRequest;
import com.microservice_auth.request.UpdateUserRequest;
import com.microservice_auth.service.IUserService;
import com.microservice_auth.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.microservice_auth.constants.ErrorMessageConstants.EMAIL_ALREADY_REGISTERED;
import static com.microservice_auth.constants.ErrorMessageConstants.EMAIL_REGISTERED_BUT_INACTIVE;
import static com.microservice_auth.constants.GeneralConstants.*;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final UserMapper userMapper;

    private final EmailVerificationProducer emailVerificationProducer;
    private final IRoleRepository roleRepository;


    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toDto(findUserById(id));
    }


    @Transactional
    @Override
    public UserDto createUser(CreateUserRequest userRequest)  {
        Optional<User> userOptional = userRepository.findByEmail(userRequest.getEmail());

        if(userOptional.isPresent()){
            User user = userOptional.get();

            if(user.getAccountStatus().equals(AccountStatus.ACTIVE)){
                throw new EmailAlreadyRegisteredException(EMAIL_ALREADY_REGISTERED);
            }

            throw new EmailAlreadyRegisteredException(EMAIL_REGISTERED_BUT_INACTIVE);
        }

        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User user = userMapper.toEntity(userRequest);

        user.setRole(getDefaultUserRole());
        user.setAccountStatus(AccountStatus.PENDING_VERIFICATION);

        user.setAvatarUrl(userRequest.getAvatarFile() != null ? uploadFile(userRequest.getAvatarFile()) : null);

        User savedUser = userRepository.save(user);
        emailVerificationProducer.sendVerificationTokenByEmail(savedUser);
        return userMapper.toDto(savedUser);
    }


    @Transactional
    @Override
    public UserDto updateUser(Long userId, UpdateUserRequest userRequest) {

        User existingUser = findUserById(userId);

        existingUser.setAvatarUrl(
                handleAvatar(userRequest.getAvatarFile(), userRequest.isRemoveAvatar(), existingUser.getAvatarUrl())
        );

        existingUser.setFullName(userRequest.getFullName());
        existingUser.setPhoneNumber(userRequest.getPhoneNumber());

        return userMapper.toDto(userRepository.save(existingUser));
    }

    @Override
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName(), FIELD_EMAIL, email));
    }

    @Override
    public void updateUserAccountStatus(Long userId, AccountStatus accountStatus){
        userRepository.updateAccountStatus(userId, accountStatus);
    }

    @Override
    public Role getDefaultUserRole() {
        return roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException(Role.class.getSimpleName(), FIELD_NAME,
                        RoleName.ROLE_USER.name()));
    }

    private User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName(), FIELD_ID, id));
    }

    private String handleAvatar(MultipartFile file, boolean removeAvatar, String currentAvatar) {
        if (removeAvatar && currentAvatar != null) {
            s3Service.deletePreviousImageFromS3(currentAvatar);
            return null;
        }
        return file != null ? uploadFile(file) : currentAvatar;
    }

    private String uploadFile(MultipartFile file){
        return s3Service.uploadFile(file);
    }



}
