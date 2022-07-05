package com.hanghae0705.sbmoney.security.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

// 인증 정보 redirectUri를 저장하고 가져올 수 있게 하는 역할
//@ConfigurationProperties(prefix="app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oAuth2 = new OAuth2();

    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public long getTokenExpirationMsec() {
            return tokenExpirationMsec;
        }

        public void setTokenExpirationMsec(long tokenExpirationMsec) {
            this.tokenExpirationMsec = tokenExpirationMsec;
        }
    }

    public static class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

    public Auth getAuth() {
        return auth;
    }

    public OAuth2 getoAuth2() {
        return oAuth2;
    }
}
