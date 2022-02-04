package com.stopclimatechange.earthgarden.service;

import com.stopclimatechange.earthgarden.domain.User;
import com.stopclimatechange.earthgarden.domain.UserDto;
import com.stopclimatechange.earthgarden.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    public User signUp(UserDto userDto, MultipartFile image) {
        userDto.setRoles(Collections.singletonList("ROLE_USER"));
        userDto.setPw(passwordEncoder.encode(userDto.getPw()));           // 비밀번호 암호화
        if(!image.isEmpty())
            userDto.setImage_url(imageUploadService.restore(image));
        return userRepository.save(new User(userDto));
    }

    @Override
    public User signIn(UserDto.LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseGet(() -> null );             // 이메일로 user 검색

        if(user==null)
            return null;
        else if (!passwordEncoder.matches(loginDto.getPw(), user.getPw()))           // 패스워드 확인
            return null;

        return user;
    }

    @Override
    public Boolean validateDuplicateEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean validateDuplicateNickname(String nickname){
        return userRepository.existsByNickname(nickname);
    }

}
