package com.stopclimatechange.earthgarden.service;

import com.stopclimatechange.earthgarden.domain.User;
import com.stopclimatechange.earthgarden.domain.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService {

    //회원가입
    public User signUp(String email, String pw, String nickname, MultipartFile image);
    public User signUp(UserDto.SocialSignupDto socialSignupDto);

    // 로그인할 이메일 & 패스워드 체크
    public User signIn(UserDto.LoginDto userDto);
    public User signIn(String socialType, String socialToken);

    public Boolean checkIsMember(String social_id);

    public Boolean validateDuplicateEmail(String email);

    public Boolean validateDuplicateNickname(String nickname);

    public User findUserByEmail(String email);

    public void updateProfile(User user, String nickname, MultipartFile image);

    public void updatePassword(User user, String pw);

    public void saveUpdatedUser(User user);

    public Boolean checkRightPassword(User user, String ori_pw);
}