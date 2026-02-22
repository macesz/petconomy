package com.petconomy.controller;

import com.petconomy.controller.dto.*;
import com.petconomy.model.user.Member;
import com.petconomy.security.jwt.JwtUtils;
import com.petconomy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
public class MemberController {

    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.memberService = memberService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberCredentialsDto loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();
        Member loggedMember = memberService.findMemberByEmail(userDetails.getUsername());
        return ResponseEntity
                .ok(new JwtResponse(jwt, loggedMember.getName(), roles));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody MemberRegistrationDto signUpRequest) {

            return memberService.register(signUpRequest, encoder);
    }

    @GetMapping("/profile")
//    @PreAuthorize("isAuthenticated()")
    public MemberProfileDto getProfile() {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Get the member associated with the email
        Member currentMember = memberService.findMemberByEmail(currentUserEmail);

        return new MemberProfileDto(currentMember.getId(), currentMember.getName(), currentMember.getEmail(), currentMember.getTargetAmount());
    }

    @GetMapping("/{id}")
    public MemberDto getUser(@PathVariable int id) {
        return memberService.getMember(id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable int id) {
        return memberService.deleteMember(id);
    }


    @PutMapping("/profile/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateProfileDto profileDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Member currentMember = memberService.findMemberByEmail(currentUserEmail);

        if(profileDto.currentPassword() != null && !profileDto.currentPassword().isEmpty()
                && profileDto.newPassword() != null && !profileDto.newPassword().isEmpty()) {

            if (!encoder.matches(profileDto.currentPassword(), currentMember.getPassword())) {
                return ResponseEntity.badRequest().body("Wrong current password");
            }

            currentMember.setPassword(encoder.encode(profileDto.newPassword()));
        }

        if (profileDto.username() != null) {
            currentMember.setName(profileDto.username());
        }

        if (profileDto.newTargetAmount() != null) {
            currentMember.setTargetAmount(profileDto.newTargetAmount());
        }

        boolean updated = memberService.updateMember(currentMember);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Failed to update user");
        }
    }

//    @GetMapping("/currentPokemon")
//    public MyPokemonDto getMyPokemon(){
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return memberService.getMyPokemon(user.getUsername());
//    }

    @GetMapping("/savings")
    public int getMySavings(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return memberService.getMySaving(user.getUsername());
    }
}
