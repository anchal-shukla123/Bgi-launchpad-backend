package com.bgi.launchpad.service;

import com.bgi.launchpad.dto.request.LoginRequestDTO;
import com.bgi.launchpad.dto.request.RegisterRequestDTO;
import com.bgi.launchpad.dto.response.AuthResponseDTO;
import com.bgi.launchpad.dto.response.UserResponseDTO;
import com.bgi.launchpad.exception.BadRequestException;
import com.bgi.launchpad.model.User;
import com.bgi.launchpad.repository.UserRepository;
import com.bgi.launchpad.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for handling authentication operations (login, register).
 */
@Service
@Transactional
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository, 
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Register a new user
     */
    public AuthResponseDTO register(RegisterRequestDTO request) {
        logger.info("Registering new user with email: {}", request.getEmail());

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already registered");
        }

        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setDepartmentId(request.getDepartmentId());
        user.setIsActive(true);

        // Save user
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: {}", savedUser.getId());

        // Generate tokens
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        // Convert to response DTO
        UserResponseDTO userResponse = convertToUserResponseDTO(savedUser);

        return new AuthResponseDTO(token, refreshToken, userResponse);
    }

    /**
     * Authenticate user and generate tokens
     */
    public AuthResponseDTO login(LoginRequestDTO request) {
        logger.info("Login attempt for email: {}", request.getEmail());

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );

            // Load user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generate tokens
            String token = jwtUtil.generateToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            // Load user entity
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            // Convert to response DTO
            UserResponseDTO userResponse = convertToUserResponseDTO(user);

            logger.info("User logged in successfully: {}", request.getEmail());

            return new AuthResponseDTO(token, refreshToken, userResponse);

        } catch (Exception e) {
            logger.error("Login failed for email: {}", request.getEmail(), e);
            throw new BadRequestException("Invalid email or password");
        }
    }

    /**
     * Refresh access token using refresh token
     */
    public AuthResponseDTO refreshToken(String refreshToken) {
        logger.info("Refreshing token");

        try {
            // Validate refresh token
            if (!jwtUtil.isTokenValid(refreshToken)) {
                throw new BadRequestException("Invalid or expired refresh token");
            }

            // Extract username
            String email = jwtUtil.extractUsername(refreshToken);

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Generate new access token
            String newToken = jwtUtil.generateToken(userDetails);

            // Load user entity
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BadRequestException("User not found"));

            // Convert to response DTO
            UserResponseDTO userResponse = convertToUserResponseDTO(user);

            logger.info("Token refreshed successfully for user: {}", email);

            return new AuthResponseDTO(newToken, refreshToken, userResponse);

        } catch (Exception e) {
            logger.error("Token refresh failed", e);
            throw new BadRequestException("Invalid refresh token");
        }
    }

    /**
     * Convert User entity to UserResponseDTO
     */
    private UserResponseDTO convertToUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setDepartmentId(user.getDepartmentId());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}