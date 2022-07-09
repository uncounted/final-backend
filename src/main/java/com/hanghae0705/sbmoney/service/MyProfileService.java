package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.ApiException;
import com.hanghae0705.sbmoney.exception.ApiRequestException;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.UserRepository;
import com.hanghae0705.sbmoney.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
public class MyProfileService {
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    public MyProfileService(UserRepository userRepository, S3Uploader s3Uploader) {
        this.userRepository = userRepository;
        this.s3Uploader = s3Uploader;
    }

    public Message getProfile() {
        User.ResponseProfile responseProfile = new User.ResponseProfile();
        responseProfile.setNickname(getUser().getNickname());
        responseProfile.setProfileImg(getUser().getProfileImg());
        responseProfile.setEmail(getUser().getEmail());
        responseProfile.setNickname(getUser().getNickname());
        return new Message(true, "조회에 성공했습니다", responseProfile);
    }

    @Transactional
    public Message updateProfile(User.RequestProfile requestProfile, MultipartFile profileImg) throws IOException {
        String imgUrl = null;
        if (profileImg != null) {
            imgUrl = s3Uploader.upload(profileImg, "static");
            getUser().updateProfile(requestProfile, imgUrl);
        } else {
            getUser().updateProfile(requestProfile);
        }
        User.ResponseProfile responseProfile = new User.ResponseProfile();
        responseProfile.setEmail(getUser().getEmail());
        responseProfile.setProfileImg(imgUrl);
        responseProfile.setIntroDesc(getUser().getIntroDesc());
        responseProfile.setNickname(getUser().getNickname());
        return new Message(true, "프로필이 변경되었습니다", responseProfile);
    }

    public User getUser() {
        return userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(
                () -> new ApiRequestException(ApiException.NOT_EXIST_USER));
    }

//    public void checkValueIsEmpty(Object target) {
//        if (target == null) {
//            throw new NullPointerException("내용이 없음");
//        }
//    }
}