package com.stopclimatechange.earthgarden.controller;

import com.stopclimatechange.earthgarden.config.JwtTokenProvider;
import com.stopclimatechange.earthgarden.domain.User;
import com.stopclimatechange.earthgarden.domain.UserDto;
import com.stopclimatechange.earthgarden.repository.UserRepository;
import com.stopclimatechange.earthgarden.service.MailService;
import com.stopclimatechange.earthgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;

@CrossOrigin
@Controller
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/user/signup",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HashMap> create(@RequestPart(value = "user") UserDto userDto, @RequestPart(value = "image", required = false) MultipartFile image) {

        User user = userService.signUp(userDto, image);

        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("message", "회원가입 성공");
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);

    }

    @PostMapping("/user/signin")
    public ResponseEntity<HashMap> login(@RequestBody UserDto.LoginDto loginDto) {

        User user = userService.signIn(loginDto);
        HashMap<String, Object> responseMap = new HashMap<>();
        if (user != null) {
            responseMap.put("status", 200);
            responseMap.put("message", "로그인 성공");
            responseMap.put("token", jwtTokenProvider.createToken(user.getEmail(), user.getRoles()));
            System.out.println(user.getRoles().toString());
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        }
        else {
            responseMap.put("status", 401);
            responseMap.put("message", "이메일 또는 비밀번호 오류");
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.UNAUTHORIZED);
        }
    }
}