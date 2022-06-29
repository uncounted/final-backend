package com.hanghae0705.sbmoney.security;

import com.hanghae0705.sbmoney.exception.ApiException;
import com.hanghae0705.sbmoney.exception.ApiRuntimeException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil(){}

    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    public static Long getCurrentUserId(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null){
            throw new ApiRuntimeException(ApiException.NOT_EXIST_IN_SECURITY_CONTEXT);
        }

        //user의 id 가 리턴됨(security에서 id를 스트링으로 저장하나?)
        return Long.parseLong(authentication.getName());
    }
}
