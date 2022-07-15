package com.tsp.new_tsp_project.api.jwt;

import com.tsp.new_tsp_project.api.admin.user.service.AdminUserApiService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static io.jsonwebtoken.Jwts.*;
import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.claims;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.lang.System.currentTimeMillis;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final MyUserDetailsService myUserDetailsService;
    private final AdminUserApiService adminUserApiService;

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    private Key getSigningKey(String secretKey) {
        return hmacShaKeyFor(secretKey.getBytes(UTF_8));
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * <pre>
     * 1. MethodName : doGenerateToken
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : 토큰 발급
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 07. 07.
     * </pre>
     */
    public String doGenerateToken(String username, long expireTime) {
        Claims claims = claims();
        claims.put("username", username);

        return builder()
                .setClaims(claims)
                .setIssuedAt(new Date(currentTimeMillis()))
                .setExpiration(new Date(currentTimeMillis() + expireTime))
                .signWith(HS256, getSigningKey(SECRET_KEY))
                .compact();
    }

    /**
     * <pre>
     * 1. MethodName : extractAllClaims
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : JWT 토큰이 유효한 토큰인지 검사한 후, 토큰에 담긴 Payload 값을 가져온다
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 07. 07.
     * </pre>
     */
    public Claims extractAllClaims(String token) throws ExpiredJwtException {
        return parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * <pre>
     * 1. MethodName : isTokenExpired
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : 만료된 토큰인지 체크
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 07. 07.
     * </pre>
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * <pre>
     * 1. MethodName : generateToken
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : 토큰 재발급
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 07. 07.
     * </pre>
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * <pre>
     * 1. MethodName : createToken
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : JWT 토큰 생성
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 07. 07.
     * </pre>
     */
    private String createToken(Map<String, Object> claims, String subject) {
        byte[] keyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        SignatureAlgorithm signatureAlgorithm = HS256;
        Key keys = new SecretKeySpec(keyBytes, signatureAlgorithm.getJcaName());

        return builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(currentTimeMillis()))
                .setExpiration(new Date(currentTimeMillis() + 1000 * 60 * 60 * 10)).signWith(signatureAlgorithm, keys).compact();
    }

    /**
     * <pre>
     * 1. MethodName : resolveToken
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : Header 토큰 정보 가져오기
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 07. 07.
     * </pre>
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public String resolveAccessToken(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null && !Objects.equals(request.getHeader("Authorization"), "")) {
            return request.getHeader("Authorization");
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        if (request.getHeader("refreshToken") != null && !Objects.equals(request.getHeader("refreshToken"), "")) {
            return request.getHeader("refreshToken");
        }
        return null;
    }

    /**
     * <pre>
     * 1. MethodName : validateToken
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : JWT 토큰 검증
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 07. 07.
     * </pre>
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    // JWT 에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(adminUserApiService.findOneUserByToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 엑세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Authorization", accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("refreshToken", refreshToken);
    }
}
