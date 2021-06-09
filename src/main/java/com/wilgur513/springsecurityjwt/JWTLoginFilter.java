package com.wilgur513.springsecurityjwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final ObjectMapper objectMapper;

    public JWTLoginFilter(AuthenticationManager authenticationManager, JWTUtils jwtUtils, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/login");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserLogin userLogin = objectMapper.readValue(request.getInputStream(), UserLogin.class);
        System.out.println(userLogin);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userLogin.getUsername(), userLogin.getPassword()
        );
        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException
    {
        User user = (User)authResult.getPrincipal();
        response.addHeader("Authentication", "Bearer " + jwtUtils.generate(user.getUsername()));

    }
}
