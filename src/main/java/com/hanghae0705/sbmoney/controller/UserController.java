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


//    @PostMapping("/api/user/login")
//    public JwtTokenDto login(@RequestBody User.RequestLogin requestLogin) throws UserException {
//        return userService.login(requestLogin));
//    }

    //로그인된 유저의 데이터를 반환
    @GetMapping("/api/user/myInfo")
    public User.Response getMyInfo(){
        return userService.getMyInfo();
    }

    //회원가입
    @PostMapping("/api/user/register")
    public RespDto registerUser(@RequestBody User.Request requestDto){
        userService.saveUser(requestDto);
        return RespDto.builder()
                .result(true)
                .respMsg("회원가입에 성공했습니다.")
                .build();
    }

    @PostMapping("/api/user/login")
    public TokenDto login(@RequestBody User.RequestLogin memberRequestDto) {
        return userService.login(memberRequestDto);
    }

    @PostMapping("/api/user/reissue")
    public TokenDto reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return userService.reissue(tokenRequestDto);
    }
}
