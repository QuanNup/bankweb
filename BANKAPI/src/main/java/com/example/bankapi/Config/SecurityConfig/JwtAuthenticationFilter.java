package com.example.bankapi.Config.SecurityConfig;

import com.example.bankapi.Config.GlobalConfig.StaticVar;
import com.example.bankapi.Service.Authentication.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver exceptionResolver;


    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain) throws ServletException, IOException {
            final String authHeader = request.getHeader("Authorization");
           try {
               logger.debug("Authorization header found: {}", authHeader); // Log header Authorization

               //xử lí request không có token
               if ((authHeader == null || !authHeader.startsWith("Bearer "))) {
                   filterChain.doFilter(request, response);
                   return;
               }

               final String token = authHeader.substring(7);
               final String userEmail = jwtService.extractUserName(token);
               logger.debug("Extracted token: {}", token);
               logger.debug("Extracted userEmail: {}", userEmail);
               if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                   if(!jwtService.isBlackListed(token)){
                       UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                       logger.debug("Token is not blacklisted"); // Log thông báo nếu token không trong blacklist

                       // ... (Phần tải UserDetails) ...
                       logger.debug("Loaded userDetails: {}", userDetails);
                       if (jwtService.isValidToken(token, userDetails)) {
                           UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                   userDetails,
                                   null,
                                   userDetails.getAuthorities()
                           );
                           SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                       }
                   }else{
                        logger.warn("Token is blacklisted: {}", token);
                        throw new AccessDeniedException(StaticVar.BLACKLISTED_TOKEN_MESSAGE);

                   }
               }
               logger.debug("Passing request to filter chain");
               filterChain.doFilter(request, response);
           }catch (Exception ex){
               exceptionResolver.resolveException(request,response,null,ex);
           }
    }
}
