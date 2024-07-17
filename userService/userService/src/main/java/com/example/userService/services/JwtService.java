package com.example.userService.services;

import com.example.userService.entity.Token;
import com.example.userService.entity.User;
import com.example.userService.repository.TokenRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtService {


    private final String secret_key = "mysecretkey";

    private final long accessTokenValidity = 30L * 24 * 60 * 60 * 1000;

    private final JwtParser jwtParser;

    @Autowired
    private TokenRepository tokenRepository;


    public JwtService()
    {
        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
    }

    public String createToken(User user , Map<String , Object> extraClaims) {


        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }


    public String updateToken(String token, Map<String, Object> updatedClaims) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret_key)
                .parseClaimsJws(token)
                .getBody();

        // Merge the updated claims with existing claims
        claims.putAll(updatedClaims);

        // Optionally update the expiration date
        claims.setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(claims.getSubject())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(claims.getExpiration())
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    private Claims parseJwtClaims(String token)
    {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String resolveToken(HttpServletRequest request)
    {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public Claims resolveClaims(HttpServletRequest req)
    {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }


    public boolean isTokenExpired(Date expirationDate) throws AuthenticationException
    {
        try {
            if(expirationDate.before(new Date()))
                return true;
            else
                return false;
        } catch (Exception e) {
            throw e;
        }
    }


    public boolean isTokenValid(String accessToken , UserDetails userDetails) {
        String username = userDetails.getUsername();
        Claims claims = parseJwtClaims(accessToken);
        return username.equals(claims.getSubject()) && !isTokenExpired(claims.getExpiration());
    }


    public boolean validateToken(String token) {
        Token tokenEntity = tokenRepository.findByToken(token);
        return tokenEntity != null && !(tokenEntity.isExpired()) && !(tokenEntity.isRevoked());
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }


    public String getTokenById(int id) {
        Optional<Token> tokenOptional = tokenRepository.findTokensByUser(id);
        if (tokenOptional.isPresent()) {
            return tokenOptional.get().getToken();
        } else {
            return null;
        }
    }


}
