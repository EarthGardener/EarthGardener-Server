package com.stopclimatechange.earthgarden.controller;

import com.stopclimatechange.earthgarden.config.JwtTokenProvider;
import com.stopclimatechange.earthgarden.domain.*;
import com.stopclimatechange.earthgarden.service.PostService;
import com.stopclimatechange.earthgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        List<PostDto> posts = postService.findFitPost(user, date);

        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("message", "글 목록 조회 성공");
        responseMap.put("data", posts);
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
    }

    @GetMapping(value = "/posts/new/checklist")
    public ResponseEntity<HashMap> getChecklist() {

        List<CheckMent> checkMents = postService.chooseMents();

        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("message", "체크리스트 조회 성공");
        responseMap.put("data", checkMents);
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
    }


}
