package com.stopclimatechange.earthgarden.controller;

import com.stopclimatechange.earthgarden.config.JwtTokenProvider;
import com.stopclimatechange.earthgarden.domain.TreeDto;
import com.stopclimatechange.earthgarden.domain.User;
import com.stopclimatechange.earthgarden.service.TreeService;
import com.stopclimatechange.earthgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class TreeController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TreeService treeService;

    @PostMapping(value = "/tree/name")
    public ResponseEntity<HashMap> giveNameToTree(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody TreeDto.NameDto nameDto) {

        User user = userService.findUserByEmail(jwtTokenProvider.getUserEmail(token));
        treeService.changeTreeName(user, nameDto);

        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("message", nameDto.getName() + "으로 이름 변경 성공");
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
    }

    @GetMapping(value = "/tree/info")
    public ResponseEntity<HashMap> giveNameToTree(@RequestHeader("X-AUTH-TOKEN") String token) {

        User user = userService.findUserByEmail(jwtTokenProvider.getUserEmail(token));
        TreeDto treeDto = treeService.returnTreeInfo(user);
        Boolean levelUp = treeService.isTreeLevelUp(treeDto);
        if(levelUp) treeDto = treeService.reflectLevel(user);
        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("message", "나무 정보 불러오기 성공");
        responseMap.put("levelup", levelUp);
        responseMap.put("user_nickname", user.getNickname());
        responseMap.put("data", treeDto);
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
    }
}
