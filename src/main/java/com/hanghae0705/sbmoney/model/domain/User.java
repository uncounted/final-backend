package com.hanghae0705.sbmoney.model.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hanghae0705.sbmoney.model.domain.baseEntity.BaseEntity;
import com.hanghae0705.sbmoney.model.domain.baseEntity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String nickname;

    @NotNull
    private String email;

    @NotNull
    private String profileImg;

    @NotNull
    private String introDesc;

    @NotNull
    private String provider; //general, google, kakao

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @NotNull
    private LocalDateTime lastEntered;

    @Builder
    public User(Long id, String username, String password, String nickname, String email, String profileImg, String introDesc, String provider, UserRoleEnum role, LocalDateTime lastEntered) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.profileImg = profileImg;
        this.introDesc = introDesc;
        this.provider = provider;
        this.role = role;
        this.lastEntered = lastEntered;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changeProfileImg(String imgUrl){
        this.profileImg = imgUrl;
    }

    public void changeIntroDesc(String desc){
        this.introDesc = desc;
    }

    public void changeLastEntered(LocalDateTime lastEntered){
        this.lastEntered = lastEntered;
    }
}
