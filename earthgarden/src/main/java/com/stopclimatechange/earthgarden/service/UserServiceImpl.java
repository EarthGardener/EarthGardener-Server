package com.stopclimatechange.earthgarden.service;

import com.stopclimatechange.earthgarden.config.JwtTokenProvider;
import com.stopclimatechange.earthgarden.domain.RefreshToken;
import com.stopclimatechange.earthgarden.domain.Tree;
import com.stopclimatechange.earthgarden.domain.User;
import com.stopclimatechange.earthgarden.domain.UserDto;
import com.stopclimatechange.earthgarden.repository.RefreshTokenRepository;
import com.stopclimatechange.earthgarden.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Collections;

@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;                              // 패스워드 인코더
    private final ImageUploadService imageUploadService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KakaoService kakaoService;

    @Override
    public User signUp(String email, String pw, String nickname, MultipartFile image) {
        UserDto userDto = new UserDto();
        userDto.setRoles(Collections.singletonList("ROLE_USER"));
        userDto.setSocialId("local");
        userDto.setEmail(email);
        userDto.setNickname(nickname);
        userDto.setPw(passwordEncoder.encode(pw));           // 비밀번호 암호화
        Tree tree = new Tree();
        if(!image.isEmpty())
            userDto.setImage_url(imageUploadService.restore(image));
        return userRepository.save(new User(userDto, tree));
    }

    @Override
    public User signUp(UserDto.SocialSignupDto socialSignupDto) {
        String socialId = null;
        if(socialSignupDto.getSocialType().equals("kakao"))
            socialId = kakaoService.getUserId(socialSignupDto.getSocialToken());

        if(socialId == null)
            return null;

        UserDto userDto = new UserDto();
        userDto.setRoles(Collections.singletonList("ROLE_USER"));
        userDto.setSocialId("kakao/" + socialId);
        userDto.setEmail(socialSignupDto.getEmail());
        userDto.setNickname(socialSignupDto.getNickname());
        if(socialSignupDto.getImage_url() == null)
            userDto.setImage_url(null);
        else if(socialSignupDto.getImage_url().length()==0)
            userDto.setImage_url(null);
        else
            userDto.setImage_url(socialSignupDto.getImage_url());
        Tree tree = new Tree();
        return userRepository.save(new User(userDto, tree));
    }

    @Override
    public User signIn(UserDto.LoginDto loginDto) {
        User user = findUserByEmail(loginDto.getEmail());             // 이메일로 user 검색

        if(user==null)
            return null;
        else if (!passwordEncoder.matches(loginDto.getPw(), user.getPw()))           // 패스워드 확인
            return null;

        RefreshToken refreshToken;
        if(!refreshTokenRepository.existsByUserId(user.getId())){
            refreshToken = new RefreshToken(user.getId(), jwtTokenProvider.createRefreshToken());
            refreshTokenRepository.save(refreshToken);
        }
        else {
            refreshToken = refreshTokenRepository.findByUserId(user.getId()).orElseGet(null);
            refreshTokenRepository.save(refreshToken);
        }
        return user;
    }

    public String giveRefreshToken(User user){
        return refreshTokenRepository.findByUserId(user.getId()).orElseGet(null).getRefreshToken();
    }

    @Override
    public User reissueTokenByRefreshToken(String token, String refreshToken){

        if(!jwtTokenProvider.validateTokenExceptExpiration(token)){
            return null;
        }

        if(!jwtTokenProvider.validateToken(refreshToken)){
            return null;
        }

        RefreshToken refreshTokenObject = refreshTokenRepository.findByRefreshToken(refreshToken).orElseGet(null);
        if(refreshTokenObject == null){
            return null;
        }

        User user = userRepository.findById(refreshTokenObject.getUserId()).orElseGet(null);
        if(user == null){
            return null;
        }
        return user;
    }

    @Override
    public User signIn(String socialType, String socialToken) {
        String socialId = null;
        if(socialType.equals("kakao"))
            socialId = kakaoService.getUserId(socialToken);

        if(socialId == null)
            return null;

        socialId = "kakao/" + socialId;
        User user = userRepository.findBySocialId(socialId).orElseGet(()->null);
        return user;
    }


    public Boolean checkIsMember(String socialId){
        return userRepository.existsBySocialId("kakao/" + socialId);
    }

    @Override
    public void updateProfile(User user, String nickname, MultipartFile image){
        imageUploadService.deleteImage(user.getImage_url());

        if(!image.isEmpty())
            user.updateProfile(nickname, imageUploadService.restore(image));
        else
            user.updateProfile(nickname, null);

        userRepository.save(user);
    }

    @Override
    public void updatePassword(User user, String pw){
        user.updatePw(passwordEncoder.encode(pw));
        userRepository.save(user);
    }

    @Override
    public Boolean checkRightPassword(User user, String ori_pw){
        if(passwordEncoder.matches(ori_pw , user.getPw()))
            return true;
        else return false;
    }

    @Override
    public Boolean validateDuplicateEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean validateDuplicateNickname(String nickname){
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseGet(()->null);
    }

    @Override
    public void saveUpdatedUser(User user){
        userRepository.save(user);
    }

}
