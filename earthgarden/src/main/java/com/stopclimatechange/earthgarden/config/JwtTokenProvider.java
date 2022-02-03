package com.stopclimatechange.earthgarden.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/*
 * JWT Token 생성, 인증, 권한 부여, 유효성 검사, PK 추출 등의 다양한 기능을 제공하는 Provider 클래스
 */
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("spring.jwt.secret")                     // application.properties에서 secretKey 주입
    private String secretKey = "earthGardener-jwt-authorization";

    /* 토큰 유효시간 */
    private long tokenValidTime = 60 * 180 * 1000L;  // 3시간

    private final UserDetailsService userDetailsService;

    /* 객체 초기화 (secretKey를 Base64로 인코딩) */
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /* JWT 토큰 생성 */
    public String createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk);   // JWT payload에 저장되는 정보단위
        claims.put("roles", roles);                         // 정보는 key / value 쌍으로 저장
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)                          // 정보 저장
                .setIssuedAt(now)                           // 토큰 발행 시간
                .setExpiration(new Date(now.getTime() + tokenValidTime))    // 유효 시간
                .signWith(SignatureAlgorithm.HS256, secretKey)              // 사용할 암호화 알고리즘과 secret 값
                .compact();
    }

    /* 인증 성공 시 SecurityContextHolder에 저장할 Authentication 객체 생성 */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /* 토큰에서 회원 정보 추출 */
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /* Request의 Header에서 token 값 가져오기 ("X-AUTH-TOKEN": "TOKEN VALUE") */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    /* 토큰의 유효성과 만료일자 확인 */
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
