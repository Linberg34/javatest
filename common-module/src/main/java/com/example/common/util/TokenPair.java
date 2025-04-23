package com.example.common.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TokenPair {

    private String accessToken;
    private  String refreshToken;

    public TokenPair(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


}
