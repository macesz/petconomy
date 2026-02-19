package com.petconomy.backend.controller;

import com.petconomy.backend.controller.dto.*;
import com.petconomy.backend.controller.exception.UnauthorizedException;
import com.petconomy.backend.controller.exception.UserEntityNotFoundException;
import com.petconomy.backend.model.entity.UserEntity;
import com.petconomy.backend.security.jwt.JwtUtils;
import com.petconomy.backend.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserEntityController {

    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserEntityService userEntityService;

    @Autowired
    public UserEntityController(UserEntityService memberService, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userEntityService = memberService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated UserEntitySignInDto loginRequest) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            User userDetails = (User) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .toList();
            com.petconomy.backend.model.entity.UserEntity loggedUser = userEntityService.findUserByEmail(userDetails.getUsername());

            return ResponseEntity
                    .ok(new JwtResponse(jwt, loggedUser.getUserName(), roles));
        } catch (AuthenticationException e) {
            // Using a structured error response is more informative than just the exception
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new UserEntityNotFoundException());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntityDto> createUser(@RequestBody UserEntityRegistrationDto signUpRequest) {
        UserEntityDto createdUser = userEntityService.register(signUpRequest, encoder);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdUser.id()).toUri();

        return ResponseEntity.created(location).body(createdUser);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserEntityProfileDto> getProfile() {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Get the user associated with the email
        UserEntity currentUser = userEntityService.findUserByEmail(currentUserEmail);

        UserEntityProfileDto profileDto = new UserEntityProfileDto(
                currentUser.getId(),
                currentUser.getUserName(),
                currentUser.getEmail(),
                currentUser.getTargetAmount()
        );

        return ResponseEntity.ok(profileDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntityDto> getUser(@PathVariable Long id) {
        UserEntityDto user = userEntityService.getUserEntity(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userEntityService.deleteUserEntity(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @PutMapping("/profile/update")
    public ResponseEntity<UserEntityDto> updateUser(@RequestBody UpdateProfileDto profileDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            UserEntityDto updatedUser = userEntityService.updateProfile(
                    currentUserEmail,
                    profileDto,
                    encoder
            );

            return ResponseEntity.ok(updatedUser);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/savings")
    public ResponseEntity<BigDecimal> getMySavings() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BigDecimal savings = userEntityService.getMySaving(user.getUsername());
        return ResponseEntity.ok(savings);
    }
}
