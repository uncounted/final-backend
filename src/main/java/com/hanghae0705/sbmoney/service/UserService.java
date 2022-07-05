package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.ApiException;
import com.hanghae0705.sbmoney.exception.ApiRequestException;
import com.hanghae0705.sbmoney.model.domain.RefreshToken;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.model.domain.baseEntity.UserRoleEnum;
import com.hanghae0705.sbmoney.model.dto.RespDto;
import com.hanghae0705.sbmoney.model.dto.TokenRequestDto;
import com.hanghae0705.sbmoney.repository.RefreshTokenRepository;
import com.hanghae0705.sbmoney.repository.UserRepository;
import com.hanghae0705.sbmoney.security.SecurityUtil;
import com.hanghae0705.sbmoney.model.dto.TokenDto;
import com.hanghae0705.sbmoney.security.jwt.TokenProvider;
import com.hanghae0705.sbmoney.security.CookieUtils;
import com.hanghae0705.sbmoney.util.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.hanghae0705.sbmoney.security.filter.JwtFilter.AUTHORIZATION_HEADER;
import static com.hanghae0705.sbmoney.security.filter.JwtFilter.BEARER_PREFIX;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MailService mailService;

    public void saveUser(User.RequestRegister requestRegisterDto) {
        userRepository.save(User.builder()
                .id(null)
                .username(requestRegisterDto.getUsername())
                .password(passwordEncoder.encode(requestRegisterDto.getPassword()))
                .nickname(requestRegisterDto.getNickname())
                .email(requestRegisterDto.getEmail())
                .introDesc("티끌모아 태산!")
                .profileImg("https://d29fhpw069ctt2.cloudfront.net/icon/image/84587/preview.svg")
                .lastEntered(LocalDateTime.now())
                .provider("general")
                .role(UserRoleEnum.USER)
                .build());
    }

//    public void updateSocialUser(User.RequestSocialRegister requesetDto, HttpServletRequest request) {
//        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
//            if (tokenProvider.validateToken(bearerToken.substring(7))) {
//                userRepository.updateEmailNickname(requesetDto);
//                TokenDto tokenDto = tokenProvider.generateAccessToken(authentication);
//
//            RefreshToken refreshToken = RefreshToken.builder()
//                    .key(authentication.getName())
//                    .value(tokenProvider.generateRefreshToken())
//                    .build();
//
//            refreshTokenRepository.save(refreshToken);
//
//            CookieUtils.deleteCookie(request, response, "refreshToken");
//            CookieUtils.addCookie(response, "refreshToken", refreshToken.getValue(), TokenProvider.JWT_REFRESH_TOKEN_VALID_MILLI_SEC);
//
//            } else {
//                userRepository.delete();
//            }
//        }
//    }

    public RespDto checkUser(String username) {
        Optional<User> found = userRepository.findByUsername(username);

        if (found.isEmpty()) {
            return RespDto.builder()
                    .result(true)
                    .respMsg("중복된 아이디가 없습니다. 회원가입이 가능합니다.")
                    .build();
        } else {
            return RespDto.builder()
                    .result(false)
                    .respMsg("중복된 아이디가 있어 회원가입이 불가능합니다.")
                    .build();
        }
    }

    public RespDto checkNickname(String nickname) {
        Optional<User> found = userRepository.findByNickname(nickname);

        if (found.isEmpty()) {
            return RespDto.builder()
                    .result(true)
                    .respMsg("중복된 닉네임이 없습니다. 회원가입이 가능합니다.")
                    .build();
        } else {
            return RespDto.builder()
                    .result(false)
                    .respMsg("중복된 닉네임이 있어 회원가입이 불가능합니다.")
                    .build();
        }
    }

    public RespDto checkEmail(String email) {
        Optional<User> found = userRepository.findByEmail(email);

        if (found.isEmpty()) {
            return RespDto.builder()
                    .result(true)
                    .respMsg("중복된 이메일이 없어 가입이 가능합니다.")
                    .build();
        } else {
            return RespDto.builder()
                    .result(false)
                    .respMsg("중복된 이메일이 있어 가입이 불가능합니다.")
                    .build();
        }
    }

    public Message findUsername(User.RequestUserId requestUserId) {
        Optional<User.ResponseFoundId> found = userRepository.findByEmail(requestUserId.getEmail())
                .map(User.ResponseFoundId::of);

        // .get()을 안 쓸 수 있는 방향 찾아보기
        if(found.isPresent()) {
            if(found.get().getProvider().equals("general")) {
                found.get().setUserId(found.get().getUserId().substring(0, 3) + "***");
                System.out.println(found.get().getUserId());
            }

            return Message.builder()
                    .result(true)
                    .respMsg("가입된 회원입니다.")
                    .data(found.get())
                    .build();
        } else {
            return Message.builder()
                    .result(false)
                    .respMsg("회원정보가 없습니다.")
                    .data(null)
                    .build();
        }
    }

    public RespDto findPassword(User.RequestPassword requestPassword) {

        Optional<User> found = userRepository.findByUsername(requestPassword.getUsername());

        // username의 email과 클라이언트에서 보낸 email이 일치하는지 검사
        if (found.isPresent() && found.get().getEmail().equals(requestPassword.getEmail())) {
            // 소셜로 가입된 회원이면 메일 발송하지 않기
            if (!found.get().getProvider().equals("general")) {
                return RespDto.builder()
                        .result(true)
                        .respMsg(found.get().getProvider()+" 로 가입된 회원입니다.")
                        .build();
            } else {
                String email =  found.get().getEmail();

                // 메일 인증용 토큰 발급 및 메일 발송
                // 1. 익명 사용자용 authentication 생성
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("anonymous authentication :"+authentication);

                // 2. 인증 정보를 기반으로 JWT 토큰 생성(3분 짜리)
                TokenDto tokenDto = tokenProvider.generateAccessToken(authentication, 60 * 3 * 1000);

                // 3. 메일 발송
                mailService.sendMail(email, tokenDto.getAccessToken(), requestPassword.getUsername());

                return RespDto.builder()
                        .result(true)
                        .respMsg("메일을 발송했습니다.")
                        .build();
            }
        } else {
            return RespDto.builder()
                    .result(false)
                    .respMsg("가입된 정보가 없습니다.")
                    .build();
        }
    }

    @Transactional
    public RespDto changePassword(HttpServletRequest request, User.RequestChangePassword requestChangePassword) {
        // 토큰 꺼내기
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        String accessToken = null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
             accessToken = bearerToken.substring(7);
        }

        if (!tokenProvider.validateToken(accessToken)) {
            throw new RuntimeException("Token 이 유효하지 않습니다.");
        }

//        String tokenUsername = TokenProvider.parseClaims(accessToken).getSubject();
//        System.out.println("tokenUsername: "+tokenUsername);
        String password = passwordEncoder.encode(requestChangePassword.getPassword());
        String username = requestChangePassword.getUsername();
        Optional<User> found = userRepository.findByUsername(username);

        // changePassword
        if (found.isPresent()) {
            found.get().changePassword(password);
            return RespDto.builder()
                    .result(true)
                    .respMsg("비밀번호를 변경했습니다.")
                    .build();
        } else {
            return RespDto.builder()
                    .result(false)
                    .respMsg("토큰에 해당하는 유저가 없습니다.")
                    .build();
        }
    }

    public Message getMyInfo() {
        Optional<User.Response> resp = userRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .map(User.Response::of);

        if (resp.isPresent()) {
            return Message.builder()
                    .result(true)
                    .respMsg("로그인 유저 정보 조회에 성공하였습니다.")
                    .data(resp)
                    .build();
        } else {
            return Message.builder()
                    .result(false)
                    .respMsg("로그인 유저 정보가 없습니다.")
                    .data(null)
                    .build();
        }
    }

    @Transactional
    public TokenDto login(User.RequestLogin requestLogin, HttpServletRequest request, HttpServletResponse response) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = requestLogin.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateAccessToken(authentication);

        // 4. RefreshToken DB에 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenProvider.generateRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        CookieUtils.deleteCookie(request, response, "refreshToken");
        CookieUtils.addCookie(response, "refreshToken", refreshToken.getValue(), TokenProvider.JWT_REFRESH_TOKEN_VALID_MILLI_SEC);

        //5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto, HttpServletRequest request, HttpServletResponse response) {
        // 1. Refresh Token 검증 - 만료 체크 jjwt 라이브러리가 해줌
        // 만약 리프레시 토큰을 탈취하고, reissue 요청을 날리면 1~5까지 다 뚫리는 거 아닌지?!
        // 방비: 탈취 방비(리프레시 토큰 http-only, secure 쿠키로 저장) & 탈취 후 방비(로그아웃 시 DB에서 리프레시 토큰 비워주기)
        // http-only는 자바스크립트로 조작 불가 / secure 쿠키는 https 가 아니면 전송하지 않는다.
        Cookie refreshTokenCookie = CookieUtils.getCookie(request, "refreshToken")
                .orElseThrow(()->new ApiRequestException(ApiException.NO_COOKIE));

        if (!tokenProvider.validateToken(refreshTokenCookie.getValue())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 username 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 username 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(refreshTokenCookie.getValue())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateAccessToken(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenProvider.generateRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        CookieUtils.deleteCookie(request, response, "refreshToken");
        CookieUtils.addCookie(response, "refreshToken", newRefreshToken.getValue(), TokenProvider.JWT_REFRESH_TOKEN_VALID_MILLI_SEC);

        // 토큰 발급
        return tokenDto;
    }
}