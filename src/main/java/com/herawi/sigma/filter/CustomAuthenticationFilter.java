package com.herawi.sigma.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.herawi.sigma.dto.LoginInformationDTO;
import com.herawi.sigma.model.Account;
import com.herawi.sigma.model.Role;
import com.herawi.sigma.service.AccountDTOMapper;
import com.herawi.sigma.service.AccountService;
import com.herawi.sigma.service.FileStorageService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AccountService accountService, AuthenticationManager authenticationManager) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email").toLowerCase();
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successful authenticate");
        String email = request.getParameter("email");
        Account account = accountService.getAccountWithDetails(email);
        Algorithm algorithm = Algorithm.HMAC256("Bearer".getBytes());
        String accessToken = JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000*60*60*24*10)))
                .withClaim("roles", account.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);
        LoginInformationDTO loginInformationDTO =
                new LoginInformationDTO(accessToken, accessToken, accountService.getAccount(email));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("accessToken", accessToken);
        response.setHeader("refreshToken", accessToken);
        new ObjectMapper().writeValue(response.getOutputStream(), loginInformationDTO);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("unsuccessful attempt");

    }
}
