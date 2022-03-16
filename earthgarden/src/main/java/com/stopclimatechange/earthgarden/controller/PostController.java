package com.stopclimatechange.earthgarden.controller;

import com.stopclimatechange.earthgarden.config.JwtTokenProvider;
import com.stopclimatechange.earthgarden.domain.*;
import com.stopclimatechange.earthgarden.service.PostService;
import com.stopclimatechange.earthgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class PostController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PostService postService;

    @GetMapping(value = "/posts")
    public ResponseEntity<HashMap> getPostsByMonth(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam("date") String date) {

        User user = userService.findUserByEmail(jwtTokenProvider.getUserEmail(token));
        HashMap<Integer, PostDto> posts = postService.findFitPost(user, date);

        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("message", "글 목록 조회 성공");
        responseMap.put("data", posts);
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
    }

    @GetMapping(value = "/posts/new/checklist")
    public ResponseEntity<HashMap> getChecklist(@RequestHeader("X-AUTH-TOKEN") String token) {

        User user = userService.findUserByEmail(jwtTokenProvider.getUserEmail(token));

        Boolean isWrited = postService.checkTodayWrited(user);

        HashMap<String, Object> responseMap = new HashMap<>();

        if(isWrited){
            responseMap.put("status", 409);
            responseMap.put("message", "오늘 작성된 글 존재");
            responseMap.put("data", null);
            responseMap.put("isWrited", isWrited);
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.CONFLICT);
        }
        else {
            List<CheckMent> checkMents = postService.chooseMents();

            responseMap.put("status", 200);
            responseMap.put("message", "체크리스트 조회 성공");
            responseMap.put("data", checkMents);
            responseMap.put("isWrited", isWrited);
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/posts/new")
    public ResponseEntity<HashMap> posting(@RequestHeader("X-AUTH-TOKEN") String token,
                                           @RequestParam("title") String title,
                                           @RequestParam(value = "checklist_1", required = false) String checklist_1,
                                           @RequestParam(value = "checklist_2", required = false) String checklist_2,
                                           @RequestParam(value = "checklist_3", required = false) String checklist_3,
                                           @RequestPart(value = "image_1", required = false) MultipartFile image_1,
                                           @RequestPart(value = "image_2", required = false) MultipartFile image_2,
                                           @RequestPart(value = "image_3", required = false) MultipartFile image_3) {

        User user = userService.findUserByEmail(jwtTokenProvider.getUserEmail(token));

        postService.posting(user, title, checklist_1, checklist_2, checklist_3, image_1, image_2, image_3);

        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("message", "post 저장 성공");
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
    }



}
