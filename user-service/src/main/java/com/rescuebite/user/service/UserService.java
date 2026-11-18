package com.rescuebite.user.service;

import com.rescuebite.user.dto.UpdateProfileRequest;
import com.rescuebite.user.dto.UserResponse;
import com.rescuebite.user.exception.ResourceNotFoundException;
import com.rescuebite.user.model.Role;
import com.rescuebite.user.model.User;
import com.rescuebite.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUserById(String id) {
        User user = findUserById(id);
        return new UserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return new UserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public UserResponse updateProfile(String id, UpdateProfileRequest request) {
        User user = findUserById(id);
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setUpdatedAt(LocalDateTime.now());
        User updated = userRepository.save(user);
        return new UserResponse(updated);
    }

    public void deleteUser(String id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    private User findUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }
}

