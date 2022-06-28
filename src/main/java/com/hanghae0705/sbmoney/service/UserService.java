package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.exception.ApiException;
import com.hanghae0705.sbmoney.exception.ApiRequestException;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.model.domain.baseEntity.UserRoleEnum;
import com.hanghae0705.sbmoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public void checkUser(Long userId){
        Optional<User> found = userRepository.findById(userId);

        if (found.isPresent()) {
            throw new ApiRequestException(ApiException.DUPLICATED_USER);
        }
    }
}
