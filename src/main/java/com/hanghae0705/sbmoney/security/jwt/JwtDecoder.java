package com.hanghae0705.sbmoney.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hanghae0705.sbmoney.exception.ApiException;
import com.hanghae0705.sbmoney.exception.ApiRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

import static com.hanghae0705.sbmoney.security.jwt.JwtTokenUtil.*;

@Component
public class JwtDecoder {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public String decodeUserid(String token){
        DecodedJWT decodedJWT = isValidToken(token)
                .orElseThrow(() -> new ApiRequestException(ApiException.NOT_VALID_TOKEN));

        Date expiredDate = decodedJWT
                .getClaim(CLAIM_EXPIRED_DATE)
                .asDate();

        Date now = new Date();
        if(expiredDate.before(now)){
            throw new ApiRequestException(ApiException.NOT_VALID_TOKEN);
        }

        String userid = decodedJWT
                .getClaim(CLAIM_USER_NAME)
                .asString();

        return userid;
    }

    private Optional<DecodedJWT> isValidToken(String token){
        DecodedJWT jwt = null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            JWTVerifier verifier = JWT
                    .require(algorithm)
                    .build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return Optional.ofNullable(jwt);
    }
}
