package com.stopclimatechange.earthgarden.controller;

import com.stopclimatechange.earthgarden.config.JwtTokenProvider;
import com.stopclimatechange.earthgarden.domain.User;
import com.stopclimatechange.earthgarden.domain.UserDto;
import com.stopclimatechange.earthgarden.service.KakaoService;
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
    private final KakaoService kakaoService;

    /* 새로생성 start*/
    @GetMapping("/login/kakao") //제대로 동작 accessToken도 확인
    public void  kakaoCallback(@RequestParam String code) {
        System.out.println("controller code :" + code);
        String access_Token = kakaoService.getKakaoAccessToken(code);
        System.out.println("controller access_token :" + access_Token);

        String userId = kakaoService.getUserId(access_Token);
        System.out.println(userId);
    }

    @PostMapping("/user/signin/social")
    public ResponseEntity<HashMap> kakaoLogin(@RequestBody UserDto.SocialSigninDto socialDto){

        HashMap<String, Object> responseMap = new HashMap<>();
        User user = userService.signIn(socialDto.getSocialType(), socialDto.getSocialToken());

        if(!kakaoService.isTokenValid(socialDto.getSocialToken())){
            responseMap.put("status", 401);
            responseMap.put("message", "유효하지 않은 토큰");
            return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
        }

        if (socialDto.getSocialType().equals("kakao")) {
            if (user == null) {
                responseMap.put("status", 404);
                responseMap.put("message", "회원 정보 없음");
                return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
            }
            else {
                responseMap.put("status", 200);
                responseMap.put("message", "로그인 성공");
                responseMap.put("token", jwtTokenProvider.createToken(user.getSocialId(), user.getRoles()));
                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
        }
        else {
            responseMap.put("status", 401);
            responseMap.put("message", "소셜 타입 오류");
            return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user/signup/social")
    public ResponseEntity<HashMap> kakaoJoin(@RequestBody UserDto.SocialSignupDto socialDto) {
        HashMap<String, Object> responseMap = new HashMap<>();

        if(!kakaoService.isTokenValid(socialDto.getSocialToken())){
            responseMap.put("status", 401);
            responseMap.put("message", "유효하지 않은 토큰");
            return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
        }

        User user = userService.signUp(socialDto);

        responseMap.put("status", 200);
        responseMap.put("message", "회원가입 성공");
        responseMap.put("token", jwtTokenProvider.createToken(user.getSocialId(), user.getRoles()));
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
    }

    /*새로 생성 end*/

    @GetMapping(value = "/user/signup/email")
    public ResponseEntity<HashMap> checkValidEmail(@RequestParam String email){

        email = email.trim();
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

        email = email.trim();

        HashMap<String, Object> responseMap = new HashMap<>();
        if(userService.validateDuplicateEmail(email)){
            responseMap.put("status", 409);
            responseMap.put("message", "잘못된 접근. 중복 여부 확인 요함");
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.CONFLICT);
        }
        else {
            userService.signUp(email, pw, nickname, image);

            responseMap.put("status", 200);
            responseMap.put("message", "회원가입 성공");
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        }
    }


//    @PostMapping("/user/signup/kakao")
//    public ResponseEntity<HashMap> kakaoJoin(@RequestBody UserDto.KakaoDto kakaoDto) {
//        HashMap<String, Object> responseMap = new HashMap<>();
//        userService.signUp(kakaoDto);
//        responseMap.put("status", 200);
//        responseMap.put("message", "회원가입 성공");
//        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
//    }
//
//    @PostMapping("/user/signin/kakao")
//    public ResponseEntity<HashMap> kakaoLogin(@RequestBody UserDto.KakaoDto kakaoDto){
//
//        HashMap<String, Object> responseMap = new HashMap<>();
//        responseMap.put("status", 200);
//        if(userService.checkIsMember(kakaoDto.getKakao_id())) {
//            User user = userService.signIn(kakaoDto.getKakao_id());
//            //가입 여부 확인
//            responseMap.put("message", "가입된 사용자");
//            responseMap.put("data", true);
//            responseMap.put("token", jwtTokenProvider.createToken(user.getEmail(), user.getRoles()));
//        }
//        else {
//            responseMap.put("message", "가입되지 않은 사용자");
//            responseMap.put("data", false);
//        }
//        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
//    }

    @PostMapping("/user/signin")
    public ResponseEntity<HashMap> login(@RequestBody UserDto.LoginDto loginDto) {

        loginDto.setEmail(loginDto.getEmail().trim());

        User user = userService.signIn(loginDto);
        HashMap<String, Object> responseMap = new HashMap<>();

        if (user != null) {
            responseMap.put("status", 200);
            responseMap.put("message", "로그인 성공");
            responseMap.put("token", jwtTokenProvider.createToken(user.getEmail(), user.getRoles()));
            responseMap.put("refresh_token", userService.giveRefreshToken(user));
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        }
        else {
            responseMap.put("status", 401);
            responseMap.put("message", "이메일 또는 비밀번호 오류");
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/user/refresh")
    public ResponseEntity<HashMap> reissueToken(@RequestHeader(value="X-AUTH-TOKEN") String token,
                                                @RequestHeader(value="REFRESH-TOKEN") String refreshToken){

        User user = userService.reissueTokenByRefreshToken(token, refreshToken);
        HashMap<String, Object> responseMap = new HashMap<>();
        if (user != null) {
            responseMap.put("status", 200);
            responseMap.put("message", "토큰 재발급 성공");
            responseMap.put("token", jwtTokenProvider.createToken(user.getEmail(), user.getRoles()));
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        }
        else{
            responseMap.put("status", 401);
            responseMap.put("message", "잘못되었거나 만료된 refresh token");
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

    @PutMapping(value = "/user/profile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HashMap> updateProfile(@RequestHeader("X-AUTH-TOKEN") String token,
                                                 @RequestPart(value="nickname") String nickname,
                                                 @RequestPart(value = "image", required = false) MultipartFile image) {

        User user = userService.findUserByEmail(jwtTokenProvider.getUserEmail(token));
        userService.updateProfile(user, nickname, image);

        HashMap<String, Object> responseMap = new HashMap<>();

        responseMap.put("status", 200);
        responseMap.put("message", "프로필 업데이트 성공");

        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
    }

    @PutMapping(value = "/user/password")
    public ResponseEntity<HashMap> updatePassword(@RequestHeader("X-AUTH-TOKEN") String token,
                                                 @RequestBody UserDto.PasswordDto passwordDto) {

        User user = userService.findUserByEmail(jwtTokenProvider.getUserEmail(token));

        HashMap<String, Object> responseMap = new HashMap<>();

        if(!userService.checkRightPassword(user, passwordDto.getOri_pw())){
            responseMap.put("status", 409);
            responseMap.put("message", "비밀번호 오류");
            
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.CONFLICT);
        }
        else{
            userService.updatePassword(user, passwordDto.getNew_pw());

            responseMap.put("status", 200);
            responseMap.put("message", "비밀번호 변경 성공");

            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        }
    }

}