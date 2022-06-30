package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.model.dto.RespDto;
import com.hanghae0705.sbmoney.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/user/register")
    public RespDto registerUser(@RequestBody User.Request requestDto){
        userService.saveUser(requestDto);
        return RespDto.builder()
                .result(true)
                .respMsg("회원가입에 성공했습니다.")
                .build();
    }
}
