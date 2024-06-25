package com.example.bankapi.DTO.Authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticateResponse {
    private String fullName;
    private String phone;
    private String token;
    private String email;
}
