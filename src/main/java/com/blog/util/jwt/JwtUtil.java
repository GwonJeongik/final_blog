package com.blog.util.jwt;

import com.blog.domain.user.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    //Authorization Header key 값
    public final static String AUTHORIZATION_HEADER = "Authorization";

    //JWT 사용시 관례
    public final static String BEARER_PREFIX = "Bearer ";

    //JWT 만료 시간
    public final static Long EXPIRATION_TIME = 60 * 60 * 1000L;

    //JWT 디코딩 된 Secret Key
    private final Key secretKey;

    /* Secret Key 인코딩 -> 디코딩 생성자 */
    public JwtUtil(@Value(value = "${JWT_SECRET_KEY}") String secretKey) {
        //인코딩 된 Secret Key -> 디코딩 Byte 배열로 반환
        byte[] decodeSecretKey = Base64.getDecoder().decode(secretKey);
        //hmac-sha 알고리즘 사용 -> Key 객체 생성
        this.secretKey = Keys.hmacShaKeyFor(decodeSecretKey);
    }

    /* JWT 생성 메서드 */
    public String createToken(Long userId, UserRole role) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId)) //JWT 주체 설정
                        .claim("role", role.name()) //JWT 에 담을 추가 정보
                        .setIssuedAt(new Date()) //발급일
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //만료일
                        .signWith(secretKey, SignatureAlgorithm.HS256) // 암호화 알고리즘
                        .compact(); //JWT 문자열 형태로 최종 반환
    }

    /* JWT -> Cookie 저장 메서드 */
    public void addJwtToCookie(String token, HttpServletResponse response) {
        //Cookie : 공백이 없어야함 : 공백을 %20으로 인코딩
        token = URLEncoder.encode(token, StandardCharsets.UTF_8).replace("\\+", "%20");

        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);
        cookie.setPath("/"); //Cookie 를 받을 경로 지정

        response.addCookie(cookie); // response -> 생성한 Cookie 추가
    }

    /* JWT SubString 추출 메서드 */
    public String substringToken(String token) {
        if (!StringUtils.hasText(token)) {
            log.error("빈 토큰={}", token);
            throw new IllegalStateException("Not Found Token");
        }

        if (!token.startsWith(BEARER_PREFIX)) {
            log.error("Bearer 로 시작하지 않음={}", token);
            throw new IllegalStateException("Not Start Bearer");
        }

        return token.substring(7);
    }

    /* JWT 검증 메서드 */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    /* JWT -> 사용자 정보 가져오는 메서드 */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder() //JWT 파서 빌드
                .setSigningKey(secretKey) //SecretKey 설정
                .build() //빌드 완료
                .parseClaimsJws(token) //토큰 파싱
                .getBody(); //Claims 추출
    }

    /* JWT Subject 조회 메서드 [userId] */
    public Long extractUserId(String token) {
        return Long.valueOf(extractClaims(token).getSubject());
    }

    /* JWT Role 조회 메서드 */
    public String extractUserRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
}
