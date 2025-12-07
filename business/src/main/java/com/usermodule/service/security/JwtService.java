package com.usermodule.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.usermodule.model.user.UserEntity;
import com.usermodule.service.system.OptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final UserDetailsImplService userDetailsImplService;
    private final OptionService optionService;

    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public UserDetails tokenValidate(String token) {
        JWTVerifier verifier = JWT.require(getSignInKey())
                .withIssuer(getIssuer(token))
                .build();
        verifier.verify(token);

        String username = getIssuer(token);
        UserDetails userDetails = userDetailsImplService.loadUserByUsername(getIssuer(token));
        if (username.equals(userDetails.getUsername())) {
            return userDetails;
        }
        return null;
    }
    public Date extractExpiration(String token) {
        return JWT.decode(token).getExpiresAt();
    }

    private Algorithm getSignInKey() {
        String secretKey = optionService.getStringValueByCode("SECRET_KEY");
        return Algorithm.HMAC512(secretKey);
    }

    public UsernamePasswordAuthenticationToken getAuthentication(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getIssuer(String token) {
        return JWT.decode(token).getIssuer();
    }

    public String generateToken(UserEntity systemUserEntity) {
        int jwtExpiration = Integer.parseInt(optionService.getStringValueByCode("TOKEN_EXPIRATION_MIN"));
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + (60000L * jwtExpiration);
        Date exp = new Date(expMillis);
        return JWT.create()
                .withJWTId(systemUserEntity.getUserId().toString())
                .withIssuer(systemUserEntity.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .withClaim("mobile_number", systemUserEntity.getMobileNumber())
                .sign(getSignInKey());
    }
}
