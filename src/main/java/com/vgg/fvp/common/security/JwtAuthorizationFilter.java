package com.vgg.fvp.common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.vgg.fvp.common.data.User;
import com.vgg.fvp.common.data.UserRepository;
import com.vgg.fvp.common.exceptions.JwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository repo;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository repo) {
        super(authenticationManager);
        this.repo = repo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        if(header == null || header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request,response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.HEADER_STRING);
        try {
            String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRETS.getBytes()))
                    .build()
                    .verify(token.replace(JwtProperties.TOKEN_PREFIX, ""))
                    .getSubject();

            if (username != null){
                User user = repo.findUserByEmail(username);
                UserPrincipal principal = new UserPrincipal(user);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, principal.getAuthorities()
                );
                return auth;
            }
            return null;
        }catch (SignatureVerificationException ex) {
            throw new JwtException("Invalid JWT signature");
        } catch (TokenExpiredException ex) {
            throw new JwtException("Expired JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtException("JWT claims string is empty.");
        }

    }
}
