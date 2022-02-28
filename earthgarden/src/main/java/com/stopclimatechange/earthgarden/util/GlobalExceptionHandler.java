package com.stopclimatechange.earthgarden.util;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<HashMap> handleSizeLimitExceededException(SizeLimitExceededException exception) {
        HashMap<String, Object> responseMap = new HashMap<>();

        responseMap.put("status", 409);
        responseMap.put("message", "5MB이하의 이미지만 등록할 수 있습니다.");
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<HashMap> handleExpiredJwtException(ExpiredJwtException exception) {
        HashMap<String, Object> responseMap = new HashMap<>();

        responseMap.put("status", 401);
        responseMap.put("message", "만료된 토큰");
        return new ResponseEntity<HashMap>(responseMap, HttpStatus.CONFLICT);
    }
}
