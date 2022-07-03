package com.hanghae0705.sbmoney.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.model.dto.RespDto;
import com.hanghae0705.sbmoney.model.dto.TokenDto;
import com.hanghae0705.sbmoney.model.dto.TokenRequestDto;
import com.hanghae0705.sbmoney.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    //@GetMapping("/login/oauth/code/google") //라이브러리에서 처리하는 부분이기 때문에 작성 불필요
    // 또한 /login/oauth/code 는 구글 API 신청 시 한 번 등록한 후 변경불가

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
    public TokenDto login(@RequestBody User.RequestLogin requestLogin, HttpServletRequest request, HttpServletResponse response) {
        return userService.login(requestLogin, request, response);
    }

    // 로그인된 유저의 데이터를 반환
    @GetMapping("/api/user/myInfo")
    public Message getMyInfo(){
        return userService.getMyInfo();
    }

    // 액세스 토큰, 리프레시 토큰 재발급
    @PostMapping("/api/user/reissue")
    public TokenDto reissue(@RequestBody TokenRequestDto tokenRequestDto, HttpServletRequest request, HttpServletResponse response) {
        return userService.reissue(tokenRequestDto, request, response);
    }

    // 아이디 찾기


    //로그인 테스트
    @GetMapping("/user/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/login.html");
        return modelAndView;
    }

}
