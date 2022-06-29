package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.exception.ApiException;
import com.hanghae0705.sbmoney.exception.ApiRequestException;
import com.hanghae0705.sbmoney.exception.ApiRuntimeException;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.model.domain.baseEntity.UserRoleEnum;
import com.hanghae0705.sbmoney.repository.UserRepository;
import com.hanghae0705.sbmoney.security.SecurityUtil;
import com.hanghae0705.sbmoney.security.jwt.JwtTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void saveUser(User.Request requestDto){
        userRepository.save(User.builder()
                        .id(null)
                        .userid(requestDto.getUserid())
                        .password(requestDto.getPassword())
                        .nickname(requestDto.getNickname())
                        .email(requestDto.getEmail())
                        .introDesc("한다면 해")
                        .profileImg("https://d29fhpw069ctt2.cloudfront.net/icon/image/84587/preview.svg")
                        .lastEntered(LocalDateTime.now())
                        .provider("general")
                        .role(UserRoleEnum.USER)
                .build());
    }

    public void checkUser(String userId){
        Optional<User> found = userRepository.findByUserid(userId);

        if (found.isPresent()) {
            throw new ApiRequestException(ApiException.DUPLICATED_USER);
        }
    }

    public User.Response getMyInfo(){
        return userRepository.findById(SecurityUtil.getCurrentUserId())
                .map(User.Response::of)
                .orElseThrow(() -> new ApiRequestException(ApiException.NOT_EXIST_USER));
    }

    public JwtTokenDto login(User.RequestLogin requestLogin) {
        UsernamePasswordAuthenticationToken authenticationToken = requestLogin.toAuthentication();

        //Authentication authentication = authenticationM
    }
}
