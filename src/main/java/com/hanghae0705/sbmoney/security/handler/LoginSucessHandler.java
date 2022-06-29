package com.hanghae0705.sbmoney.security.handler;

import com.hanghae0705.sbmoney.security.auth.UserDetailsImpl;
import com.hanghae0705.sbmoney.security.jwt.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSucessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "Bearer";

    //토큰 생성
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final String accessToken = JwtTokenUtil.generateJwtToken(userDetails);
        final String refreshToken = JwtTokenUtil.generateRefreshToken();
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + accessToken);
    }
}
