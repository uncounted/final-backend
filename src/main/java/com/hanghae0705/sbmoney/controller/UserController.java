package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.model.dto.RespDto;
import com.hanghae0705.sbmoney.model.dto.TokenDto;
import com.hanghae0705.sbmoney.model.dto.TokenRequestDto;
import com.hanghae0705.sbmoney.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/api/user/register")
    public RespDto registerUser(@RequestBody User.RequestRegister requestRegisterDto){
        userService.saveUser(requestRegisterDto);
        return RespDto.builder()
                .result(true)
                .respMsg("회원가입에 성공했습니다.")
                .build();
    }

    // 아이디 중복검사
    @PostMapping("/api/user/register/checkUsername")
    public RespDto checkUsername(@RequestBody User.RequestCheckUsername requestCheckUsername){
        return userService.checkUser(requestCheckUsername.getUsername());
    }

    // 이메일 중복검사
    @PostMapping("/api/user/register/checkEmail")
    public RespDto checkEmail(@RequestBody User.RequestCheckEmail requestCheckEmail){
        return userService.checkEmail(requestCheckEmail.getEmail());
    }

    // 닉네임 중복검사
    @PostMapping("/api/user/register/checkNickname")
    public RespDto checkNickname(@RequestBody User.RequestCheckNickname requestCheckNickname){
        return userService.checkNickname(requestCheckNickname.getNickname());
    }

    // 로그인
    @PostMapping("/api/user/login")
    public TokenDto login(@RequestBody User.RequestLogin requestLogin) {
        return userService.login(requestLogin);
    }

    // 로그인된 유저의 데이터를 반환
    @GetMapping("/api/user/myInfo")
    public User.Response getMyInfo(){
        return userService.getMyInfo();
    }

    // 액세스 토큰, 리프레시 토큰 재발급
    @PostMapping("/api/user/reissue")
    public TokenDto reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return userService.reissue(tokenRequestDto);
    }
}
