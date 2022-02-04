package com.stopclimatechange.earthgarden.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

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
    public static class EmailDto{
        @NotNull
        private String email;
    }

    @Getter
    @Setter
    public static class NicknameDto{
        @NotNull
        private String nickname;
    }
}
