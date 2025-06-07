package com.example.backend.service;

import com.example.backend.dto.response.UserDto;
import com.example.backend.exception.*;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
            throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        return save(user);
    }

    public User getByUsernameOrEmail(String usernameOrEmail) {
        if (usernameOrEmail.contains("@")) {
            return userRepository.findByEmail(usernameOrEmail)
                    .orElseThrow(AuthenticationFailedException::new);
        } else {
            return userRepository.findByUsername(usernameOrEmail)
                    .orElseThrow(AuthenticationFailedException::new);
        }
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UsernameNotFoundException::new);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(IdNotFoundException::new);
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
                    .orElseThrow(EmailNotFoundException::new);
        } else {
            return userRepository.findByUsername(usernameOrEmail)
                    .orElseThrow(UsernameNotFoundException::new);
        }
    }

    public void updateAvatar(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(IdNotFoundException::new);

        if (file.isEmpty()) {
            throw new UploadFileIsEmptyException();
        }

        if (!file.getContentType().startsWith("image")) {
            throw new InvalidFileTypeException();
        }

        String filename = "avatar_user_" + user.getId() + "_" + System.currentTimeMillis();
        String fileUrl = localStorageService.uploadFile(file, filename);

        user.setAvatarUrl(fileUrl);
        userRepository.save(user);

    }
}
