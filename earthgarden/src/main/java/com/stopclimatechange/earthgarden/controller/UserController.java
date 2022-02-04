package com.stopclimatechange.earthgarden.controller;

import com.stopclimatechange.earthgarden.config.JwtTokenProvider;
import com.stopclimatechange.earthgarden.domain.TreeDto;
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
    public ResponseEntity<HashMap> checkValidEmail(@RequestParam String email){

        Boolean isExist = userService.validateDuplicateEmail(email);

        HashMap<String, Object> responseMap = new HashMap<>();
        if(isExist){
            responseMap.put("status", 409);
            responseMap.put("code", null);
            responseMap.put("message", "중복된 이메일");
            return new ResponseEntity<HashMap> (responseMap, HttpStatus.CONFLICT);
        }
        else{
            RandomString randomString = new RandomString(6);
            String code = randomString.nextString();
            mailService.sendCheckEmail(email, code);
            responseMap.put("status", 200);
            responseMap.put("code", code);
            responseMap.put("message", "사용 가능한 이메일, 코드 발급됨");
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/user/signup/nickname")
    public ResponseEntity<HashMap> checkValidNickname(@RequestParam String nickname){

        Boolean isExist = userService.validateDuplicateNickname(nickname);

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
    public ResponseEntity<HashMap> create(@RequestPart(value = "email") String email,
                                          @RequestPart(value = "pw") String pw,
                                          @RequestPart(value = "nickname") String nickname,
                                          @RequestPart(value = "image", required = false) MultipartFile image) {

        HashMap<String, Object> responseMap = new HashMap<>();
        if(userService.validateDuplicateEmail(email)){
            responseMap.put("status", 409);
            responseMap.put("message", "잘못된 접근. 중복 여부 확인 요함");
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.CONFLICT);
        }
        else {
            User user = userService.signUp(email, pw, nickname, image);

            responseMap.put("status", 200);
            responseMap.put("message", "회원가입 성공");
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        }

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

    @GetMapping(value = "/user/profile")
    public ResponseEntity<HashMap> getProfile(@RequestHeader("X-AUTH-TOKEN") String token) {

        User user = userService.findUserByEmail(jwtTokenProvider.getUserEmail(token));
        UserDto.ProfileDto profileDto = new UserDto.ProfileDto(user);
        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("message", "조회 완료");
        responseMap.put("data", profileDto);
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
    }
}