package com.hanghae0705.sbmoney.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hanghae0705.sbmoney.security.auth.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

public final class JwtTokenProvider {
    private static final int SEC = 1;
    private static final int MINUTE = 60 * SEC;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    //access token 유효기간 - seconds, milliseconds
    private static final int JWT_ACCESS_TOKEN_VALID_SEC = 30 * MINUTE;
    private static final int JWT_ACCESS_TOKEN_VALID_MILLI_SEC = 1000 * JWT_ACCESS_TOKEN_VALID_SEC;

    //refresh token 유효기간 - seconds, milliseconds
    private static final int JWT_REFRESH_TOKEN_VALID_SEC = 10 * DAY;
    private static final int JWT_REFRESH_TOKEN_VALID_MILLI_SEC = 1000 * JWT_REFRESH_TOKEN_VALID_SEC;

    public static final String CLAIM_EXPIRED_DATE = "EXPIRED_DATE";
    public static final String CLAIM_USER_NAME = "USER_NAME";
    private static final String AUTHORITIES_KEY = "auth";
    public static final String TOKEN_TYPE = "Bearer";

    @Value("${jwt.secret}")
    public static String JWT_SECRET;

    @Value("${jwt.issuer}")
    public static String JWT_ISSUER;

    public static String generateJwtToken(UserDetailsImpl userDetails){
        String authority = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        String accessToken = null;

        //accessToken 생성
        accessToken = JWT.create()
                .withIssuer(JWT_ISSUER)
                .withClaim(AUTHORITIES_KEY, authority)
                .withClaim(CLAIM_USER_NAME, userDetails.getUsername())
                .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALID_MILLI_SEC))
                .sign(generateAlgorithm());

        return accessToken;
    }

    public static String generateRefreshToken() {
        String refreshToken = null;

        //refreshToken 생성
        refreshToken = JWT.create()
                .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALID_MILLI_SEC))
                .sign(generateAlgorithm());

        return refreshToken;
    }

    private static Algorithm generateAlgorithm() {
        return Algorithm.HMAC256(JWT_SECRET);
    }

}
