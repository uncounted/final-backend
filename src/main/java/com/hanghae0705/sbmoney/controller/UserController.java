package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
}
