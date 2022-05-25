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
        List<PostDto.PostInfoDto> posts = postService.findFitPost(user, date);

        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("message", "글 목록 조회 성공");
        responseMap.put("data", posts);
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
    }

    @GetMapping(value = "/posts/new/iswrited")
    public ResponseEntity<HashMap> isWrited(@RequestHeader("X-AUTH-TOKEN") String token) {

        User user = userService.findUserByEmail(jwtTokenProvider.getUserEmail(token));

        Boolean isWrited = postService.checkTodayWrited(user);

        HashMap<String, Object> responseMap = new HashMap<>();
        if (isWrited) {
            responseMap.put("message", "오늘 작성된 글 존재");
        }
        else{
            responseMap.put("message", "작성 가능");
        }
        responseMap.put("status", 200);
        responseMap.put("data", isWrited);
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
    }

    @GetMapping(value = "/posts/new/checklist")
    public ResponseEntity<HashMap> getChecklist() {


        HashMap<String, Object> responseMap = new HashMap<>();


        List<CheckMent> checkMents = postService.chooseMents();

        responseMap.put("status", 200);
        responseMap.put("message", "체크리스트 조회 성공");
        responseMap.put("data", checkMents);
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
    }

    @PostMapping(value = "/posts/new")
    public ResponseEntity<HashMap> posting(@RequestHeader("X-AUTH-TOKEN") String token,
                                           @RequestPart("title") String title,
                                           @RequestPart(value = "checklist_1", required = false) String checklist_1,
                                           @RequestPart(value = "checklist_2", required = false) String checklist_2,
                                           @RequestPart(value = "checklist_3", required = false) String checklist_3,
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

    @GetMapping("/posts/detail")
    public ResponseEntity<HashMap> posting(@RequestParam("id") String id) {

        Post post = postService.getPostById(id);
        HashMap<String, Object> responseMap = new HashMap<>();

        responseMap.put("title", post.getTitle());
        responseMap.put("checklist", postService.checkMentAndExpByChecklistId(post.getChecklist_1(), post.getChecklist_2(), post.getChecklist_3()));
        responseMap.put("postImages", postService.getPostImagesURL(post));
        responseMap.put("message", "post 불러오기 성공");
        responseMap.put("status", 200);
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);

    }


}
