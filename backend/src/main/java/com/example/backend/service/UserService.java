package com.example.backend.service;

import com.example.backend.dto.UserDto;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final LocalStorageService localStorageService;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        return save(user);
    }

    public User getByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email)
                .orElseThrow(() -> new RuntimeException("Username or email not found"));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        if (usernameOrEmail.contains("@")) {
            return userRepository.findByEmail(usernameOrEmail)
                    .orElseThrow(() -> new UsernameNotFoundException(usernameOrEmail));
        } else {
            return userRepository.findByUsername(usernameOrEmail)
                    .orElseThrow(() -> new UsernameNotFoundException(usernameOrEmail));
        }
    }

    public void updateAvatar(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println(file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new RuntimeException("Empty avatar");
        }

        if (!file.getContentType().startsWith("image")) {
            throw new RuntimeException("Invalid image type");
        }

        String filename = "avatar_user_" + user.getId() + "_" + System.currentTimeMillis();
        String fileUrl = localStorageService.uploadFile(file, filename);

        user.setAvatarUrl(fileUrl);
        userRepository.save(user);

    }
}
