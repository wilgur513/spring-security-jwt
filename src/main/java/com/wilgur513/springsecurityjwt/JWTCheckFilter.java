package com.wilgur513.springsecurityjwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JWTCheckFilter extends BasicAuthenticationFilter {
    private final JWTUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JWTCheckFilter(AuthenticationManager authenticationManager, JWTUtils jwtUtils, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
    throws IOException, ServletException {
        String token = request.getHeader("Authentication");

        if(token == null || !token.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        VerifyResult result = jwtUtils.verify(token.substring("Bearer ".length()));

        if(result.isResult()) {
            UserDetails user = userDetailsService.loadUserByUsername(result.getUserId());
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(), null, user.getAuthorities())
            );
        }

        chain.doFilter(request, response);
    }
}
