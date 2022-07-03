package com.hanghae0705.sbmoney.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class TokenDto {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;

    @Builder
    public TokenDto(String grantType, String accessToken, Long accessTokenExpiresIn) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }
}
