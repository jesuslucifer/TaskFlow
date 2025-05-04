package com.example.backend.Controller;

import com.example.backend.Models.AuthRequest;
import com.example.backend.Models.AuthResponse;
import com.example.backend.Jwt.Util.JwtUtil;
import com.example.backend.Models.User;
import com.example.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserService userService,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserInfo(@PathVariable String username) {
        // Проверяем, существует ли пользователь с таким именем
        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден!");
        }
        // Возвращаем данные пользователя
        return ResponseEntity.ok(user.get());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest authRequest) {
        // Проверяем, существует ли пользователь с таким именем
        if (userService.findByUsername(authRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с таким именем уже существует!");
        }
        // Проверяем, существует ли пользователь с таким почтовым ящиком
        else if(userService.findByEmail(authRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("На данный адрес email уже зарегистрован аккаунт!");
        }
        // Создаём нового пользователя и хэшируем пароль
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        // Сохраняем пользователя
        userService.save(user);
        return ResponseEntity.ok("Пользователь успешно зарегистрирован!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<String> tokenOptional = userService.login(request.getUsername(), request.getEmail(), request.getPassword());

        if (tokenOptional.isPresent()) {
            return ResponseEntity.ok(new AuthResponse(tokenOptional.get()));
        } else {
            return ResponseEntity.status(401).body("Неверно введен логин или пароль!");
        }
    }
}
