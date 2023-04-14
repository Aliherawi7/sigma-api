package com.herawi.sigma.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.herawi.sigma.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Service
public class JWTTools {
    @Autowired
    private static AccountService accountService;



    public static boolean testJWTOfUser(HttpServletRequest request, String userEmail){
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            try {
                Algorithm algorithm = Algorithm.HMAC256("Bearer".getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(authorizationHeader);
                String email = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                Arrays.stream(roles).forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role));
                });
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                return userEmail.trim().equalsIgnoreCase(email);
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }

        }else {
            return false;
        }
    }

    public static String getUserEmailByJWT(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            try {
                Algorithm algorithm = Algorithm.HMAC256("Bearer".getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(authorizationHeader);
                return decodedJWT.getSubject();
            } catch (Exception e) {
                return null;
            }
        }else{
            return null;
        }
    }

    public static String getUsernameByJWT(HttpServletRequest request){
        String email = getUserEmailByJWT(request);
        return accountService.getAccountWithDetails(email).getUserName();
    }

    public static String createToken(String email){
        Algorithm algorithm = Algorithm.HMAC256("Bearer");
        return JWT
                .create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis()+(60*60*24*10*1000)))
                .sign(algorithm);
    }

}
