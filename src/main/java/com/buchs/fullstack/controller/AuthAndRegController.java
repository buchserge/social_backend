package com.buchs.fullstack.controller;

import com.buchs.fullstack.entity.AuthRequest;
import com.buchs.fullstack.entity.AuthResponse;
import com.buchs.fullstack.entity.UserInfo;
import com.buchs.fullstack.entity.UserInfoDetails;
import com.buchs.fullstack.exception.BadLoginException;
import com.buchs.fullstack.repo.TokenRepo;
import com.buchs.fullstack.repo.UserInfoRepository;
import com.buchs.fullstack.service.AuthenticationService;
import com.buchs.fullstack.service.JwtService;
import com.buchs.fullstack.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;


@RestController

@RequestMapping("/auth")
public class AuthAndRegController {
    @Autowired
    private TokenRepo tokenRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfo.setRoles("ROLE_USER");
        userInfoRepository.save(userInfo);

        return "User Added Successfully";
    }

    @GetMapping("/getUserName")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity getUserName(Principal principal) {

        return ResponseEntity.ok(principal.getName());
    }


    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {

        return addUser(userInfo);
    }

//    @GetMapping("/user/userProfile")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public String userProfile() {
//        return "Welcome to User Profile";
//    }
//
//    @GetMapping("/admin/adminProfile")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public String adminProfile() {
//        return "Welcome to Admin Profile";
//    }

    @PostMapping("/generateToken")
    public ResponseEntity<AuthResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest, HttpServletResponse response) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        String token = jwtService.generateToken(authRequest.getName());
        String refreshToken = jwtService.generateRefreshToken(authRequest.getName());
//        try {

        UserInfo byName = userInfoRepository.findByName(authRequest.getName()).orElseThrow(() -> new BadLoginException("bad credentials"));
        authenticationService.saveUserToken(byName, token);
//        }catch (BadLoginException e){
//            Map<String, String> error = new HashMap<>();
//            error.put("error_message", e.getErrMessage());
//            response.setContentType(APPLICATION_JSON_VALUE);
//            response.setStatus(401);
//            new ObjectMapper().writeValue(response.getOutputStream(), error);
//        };


        headers.add("Authorization", "Bearer " + token);


        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getName(),
                    authRequest.getPassword()));


//            if (authentication.isAuthenticated()) {
//                System.out.println("SUCCESS");
//
//                return jwtService.generateToken(authRequest.getName());
//            } else {
//                throw new UsernameNotFoundException("invalid user request !");
//            }
        } catch (RuntimeException e) {

            return ResponseEntity.status(401).build();

        }


        return ResponseEntity.ok().headers(headers).body((new AuthResponse("Bearer " + token, "Bearer " + refreshToken)));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody AuthResponse authResponse,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String name;
        String responseRefreshToken = authResponse.getRefreshToken();


        if (responseRefreshToken == null || !responseRefreshToken.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(new AuthResponse("BADDDDDDDDDDD111111"));
        }
        refreshToken = responseRefreshToken.substring(7);
        name = jwtService.extractUsername(refreshToken);
        if (name != null) {
            var user = userInfoRepository.findByName(name)
                    .orElseThrow();
            UserInfoDetails userInfoDetails = new UserInfoDetails(user);
            if (jwtService.isTokenValid(refreshToken, userInfoDetails)) {
                var accessToken = jwtService.generateToken(name);

                authenticationService.revokeAllUserTokens(user);
                authenticationService.saveUserToken(user, accessToken);
                return ResponseEntity.ok().body(new AuthResponse("Bearer " + accessToken, "Bearer " + refreshToken));
            }
        }
        return ResponseEntity.badRequest().body(new AuthResponse("BADDDDDDDDDDD"));
    }


}