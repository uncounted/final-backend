package com.hanghae0705.sbmoney.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hanghae0705.sbmoney.exception.ApiException;
import com.hanghae0705.sbmoney.exception.ApiRuntimeException;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.security.auth.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public final class JwtTokenUtil {
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

    public static JwtTokenDto generateJwtToken(Authentication authentication){
        String authority = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        String accessToken = null;
        String refreshToken = null;

        //accessToken 생성
        Date accessTokenExpiresIn = new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALID_MILLI_SEC)
        accessToken = JWT.create()
                .withIssuer(JWT_ISSUER)
                .withClaim(AUTHORITIES_KEY, authority)
                .withClaim(CLAIM_USER_NAME, authentication.getName())
                .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALID_MILLI_SEC))
                .sign(generateAlgorithm());


        //refreshToken 생성
        refreshToken = JWT.create()
                .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALID_MILLI_SEC))
                .sign(generateAlgorithm());

        return JwtTokenDto.builder()
                .grantType(TOKEN_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken){
        Claims claims = parseClaims(accessToken);

        if(claims.get(AUTHORITIES_KEY) == null){
            throw new ApiRuntimeException(ApiException.NO_AUTHORITY_KEY);
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities.);

    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(JWT_SECRET).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private static Algorithm generateAlgorithm() {
        return Algorithm.HMAC256(JWT_SECRET);
    }

}
