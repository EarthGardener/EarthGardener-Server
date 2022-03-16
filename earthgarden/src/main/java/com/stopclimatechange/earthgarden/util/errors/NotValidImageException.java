package com.stopclimatechange.earthgarden.util.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "유효하지 않은 이미지")
public class NotValidImageException extends RuntimeException{ }
