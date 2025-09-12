package com.simsekolah.service;

import com.simsekolah.dto.response.UserProfileResponse;
import com.simsekolah.entity.User;
import com.simsekolah.repository.UserRepository;
import com.simsekolah.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public UserProfileResponse getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserProfileResponse(
                user.getId(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                user.getUserType(),
                user.getPhone(),
                user.getAddress(),
                null, // profilePicture not in entity
                user.getCreatedAt()
        );
    }
}
