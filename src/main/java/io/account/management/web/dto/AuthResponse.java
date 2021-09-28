package io.account.management.web.dto;

import lombok.Getter;

@Getter
public class AuthResponse {
    private String accessToken;
    private String toeknType = "Bearer";

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
