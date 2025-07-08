package com.example.backend.service;

import com.example.backend.dto.response.UserDto;
import com.example.backend.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    User save(User user);

    User create(User user);

    User getByUsernameOrEmail(String usernameOrEmail);

    User getByUsername(String username);

    User getById(Long id);

    List<UserDto> getAll(String username, Pageable pageable);

    void updateAvatar(Long userId, MultipartFile file);

    UserDetails loadUserByUsername(String username);

    UserDetailsService userDetailsService();
}
