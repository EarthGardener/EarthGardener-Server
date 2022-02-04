package com.stopclimatechange.earthgarden.controller;

import com.stopclimatechange.earthgarden.config.JwtTokenProvider;
import com.stopclimatechange.earthgarden.domain.User;
import com.stopclimatechange.earthgarden.domain.UserDto;
import com.stopclimatechange.earthgarden.service.MailService;
import com.stopclimatechange.earthgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MailService mailService;

    @GetMapping(value = "/user/signup/email")
    public ResponseEntity<HashMap> checkValidEmail(@RequestBody UserDto.EmailDto emailDto){

        Boolean isExist = userService.validateDuplicateEmail(emailDto.getEmail());

        HashMap<String, Object> responseMap = new HashMap<>();
        if(isExist){
            responseMap.put("status", 409);
            responseMap.put("code", null);
            responseMap.put("message", "중복된 이메일");
            return new ResponseEntity<HashMap> (responseMap, HttpStatus.CONFLICT);
        }
        else{
            RandomString randomString = new RandomString(6);
            mailService.sendCheckEmail(emailDto.getEmail(), randomString.nextString());
            responseMap.put("status", 200);
            responseMap.put("code", randomString.nextString());
            responseMap.put("message", "사용 가능한 이메일, 코드 발급됨");
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/user/signup/nickname")
    public ResponseEntity<HashMap> checkValidNickname(@RequestBody UserDto.NicknameDto nicknameDto){

        Boolean isExist = userService.validateDuplicateNickname(nicknameDto.getNickname());

        HashMap<String, Object> responseMap = new HashMap<>();
        if(isExist){
            responseMap.put("status", 409);
            responseMap.put("message", "중복된 닉네임");
            return new ResponseEntity<HashMap> (responseMap, HttpStatus.CONFLICT);
        }
        else{
            responseMap.put("status", 200);
            responseMap.put("message", "사용 가능한 닉네임");
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        }


    }

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
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        }
        else {
            responseMap.put("status", 401);
            responseMap.put("message", "이메일 또는 비밀번호 오류");
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.UNAUTHORIZED);
        }
    }
}