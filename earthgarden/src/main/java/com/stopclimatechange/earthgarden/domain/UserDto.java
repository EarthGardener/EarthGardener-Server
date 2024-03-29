package com.stopclimatechange.earthgarden.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String socialId;

    @NotNull
    private String email;

    @NotNull
    private String pw;

    @NotNull
    private String nickname;

    private String image_url;

    private List<String> roles = new ArrayList<>();

    @Getter
    @Setter
    public static class LoginDto {
        @NotNull
        private String email;
        @NotNull
        private String pw;
    }

    @Getter
    @Setter
    public static class ProfileDto {
        @NotNull
        private String email;
        @NotNull
        private String nickname;

        private String image_url;

        public ProfileDto(User user){
            email = user.getEmail();
            nickname = user.getNickname();
            image_url= user.getImage_url();
        }
    }

    @Getter
    @Setter
    public static class PasswordDto {
        @NotNull
        private String ori_pw;
        @NotNull
        private String new_pw;
    }

    @Getter
    @Setter
    public static class SocialSignupDto{

        @NotNull
        @JsonProperty("social_token")
        private String socialToken;

        @NotNull
        @JsonProperty("social_type")
        private String socialType;

        private String nickname;
        private String image_url;
        private String email;
    }

    @Getter
    @Setter
    public static class SocialSigninDto{

        @NotNull
        @JsonProperty("social_type")
        private String socialType;

        @NotNull
        @JsonProperty("social_token")
        private String socialToken;
    }
}
