package com.stopclimatechange.earthgarden.service;

import com.stopclimatechange.earthgarden.domain.Tree;
import com.stopclimatechange.earthgarden.domain.User;
import com.stopclimatechange.earthgarden.domain.UserDto;
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

    @Override
    public User signUp(String email, String pw, String nickname, MultipartFile image) {
        UserDto userDto = new UserDto();
        userDto.setRoles(Collections.singletonList("ROLE_USER"));
        userDto.setEmail(email);
        userDto.setNickname(nickname);
        userDto.setPw(passwordEncoder.encode(pw));           // 비밀번호 암호화
        Tree tree = new Tree();
        if(!image.isEmpty())
            userDto.setImage_url(imageUploadService.restore(image));
        return userRepository.save(new User(userDto, tree));
    }

    @Override
    public User signIn(UserDto.LoginDto loginDto) {
        User user = findUserByEmail(loginDto.getEmail());             // 이메일로 user 검색

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

    @Override
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseGet(()->null);
    }

    @Override
    public void saveUpdatedUser(User user){
        userRepository.save(user);
    }
}
