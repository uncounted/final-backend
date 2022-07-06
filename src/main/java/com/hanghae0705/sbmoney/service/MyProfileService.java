package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.ApiException;
import com.hanghae0705.sbmoney.exception.ApiRequestException;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.UserRepository;
import com.hanghae0705.sbmoney.security.SecurityUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MyProfileService {

    private final UserRepository userRepository;

    public MyProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Message updateProfile(User.RequestProfile requestProfile) {
        User user = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(
                () -> new ApiRequestException(ApiException.NOT_EXIST_USER));
        user.updateProfile(requestProfile);
        User.ResponseProfile responseProfile = new User.ResponseProfile();
        responseProfile.setEmail(user.getEmail());
        responseProfile.setProfileImg(user.getProfileImg());
        responseProfile.setIntroDesc(user.getIntroDesc());
        responseProfile.setNickname(user.getNickname());
        return new Message(true, "프로필이 변경되었습니다", responseProfile);

    }

//    public void checkValueIsEmpty(Object target) {
//        if (target == null) {
//            throw new NullPointerException("내용이 없음");
//        }
//    }

    public boolean checkValisEmpty(String target) {
        return target.isEmpty();
    }


}