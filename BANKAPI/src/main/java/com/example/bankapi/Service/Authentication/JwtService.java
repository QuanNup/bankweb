package com.example.bankapi.Service.Authentication;

import com.example.bankapi.Config.GlobalConfig.StaticVar;
import com.example.bankapi.Entity.Authentication.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final String SECRET_KEY="3dfecff85e32a48ab67a977dd964d8d8848e4f048b5065d65e266d8b99b0291a" ;
    private final JwtBlacklistService blacklistService;

    public String extractUserName(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token , Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }
    public String generateToken(User user){
        return generateToken(new HashMap<>(),user);
    }
    public String generateToken(
            Map<String, Object> extraClaims,
            User user
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .claim("scope",buildScope(user))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ StaticVar.JWT_EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

    }
    public Boolean isValidToken(String token,UserDetails userDetails){
        String userName = extractUserName(token);
        return (userDetails.getUsername().equals(userName)) && !isExpiredToken(token);
    }

    private boolean isExpiredToken(String token) {
        return extractExpirationToken(token).before(new Date());
    }

    private Date extractExpirationToken(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private String buildScope(User user){
        return user.getRole().name();
    }
    public void  deleteToken(String token){
        long expiration = extractExpirationToken(token).getTime();
        blacklistService.addToBlackList(token,expiration);
    }

    public boolean isBlackListed(String token) {
        return blacklistService.isBlackListed(token);
    }
}
