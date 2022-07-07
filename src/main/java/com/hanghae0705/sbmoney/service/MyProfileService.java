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

    @Transactional
    public Message updateProfile(User.RequestProfile requestProfile, MultipartFile profileImg) throws IOException {
        User user = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(
                () -> new ApiRequestException(ApiException.NOT_EXIST_USER));
        String imgUrl = null;
        if(profileImg != null){
            imgUrl = s3Uploader.upload(profileImg, "static");
            user.updateProfile(requestProfile, imgUrl);
        } else {
            user.updateProfile(requestProfile);
        }
        User.ResponseProfile responseProfile = new User.ResponseProfile();
        responseProfile.setEmail(user.getEmail());
        responseProfile.setProfileImg(imgUrl);
        responseProfile.setIntroDesc(user.getIntroDesc());
        responseProfile.setNickname(user.getNickname());
        return new Message(true, "프로필이 변경되었습니다", responseProfile);
    }

//    public void checkValueIsEmpty(Object target) {
//        if (target == null) {
//            throw new NullPointerException("내용이 없음");
//        }
//    }
}