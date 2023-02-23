package com.herawi.sigma.tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class JWTTools {

    public static boolean testJWTOfUser(HttpServletRequest request, String userEmail){
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            try {
                Algorithm algorithm = Algorithm.HMAC256("bearer".getBytes());
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
                Algorithm algorithm = Algorithm.HMAC256("bearer".getBytes());
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

}
