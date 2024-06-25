package com.example.bankapi.Service.Authentication;

import com.example.bankapi.DTO.Authentication.AuthenticateRequest;
import com.example.bankapi.DTO.Authentication.AuthenticateResponse;
import com.example.bankapi.DTO.Authentication.RegisterRequest;
import com.example.bankapi.DTO.Authentication.RegisterResponse;
import com.example.bankapi.Entity.Authentication.ROLE;
import com.example.bankapi.Entity.Authentication.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final com.example.bankapi.Repositories.User repository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Transactional
    public RegisterResponse register(RegisterRequest request){
        if(!repository.existsByEmail(request.getEmail())){
            var user = modelMapper.map(request, User.class);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(request.getRole() == null ? ROLE.USER : ROLE.ADMIN);
            repository.save(user);
            RegisterResponse response = modelMapper.map(user,RegisterResponse.class);
            String token= jwtService.generateToken(user);
            response.setToken(token);
            return response;
        }
        return null;
    }
    @Transactional
    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail());
        AuthenticateResponse response = modelMapper.map(user, AuthenticateResponse.class);
        String token = jwtService.generateToken(user);
        response.setToken(token);
        return response;
    }
    public void logOut(String token){
        jwtService.deleteToken(token);
    }
}
