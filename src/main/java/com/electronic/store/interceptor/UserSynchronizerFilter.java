package com.electronic.store.interceptor;

import com.electronic.store.user.synchronizer.UserSynchronizer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserSynchronizerFilter extends OncePerRequestFilter {

    @Autowired
    private UserSynchronizer userSynchronizer;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            JwtAuthenticationToken token = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication());

            userSynchronizer.synchronizeWithIdp(token.getToken());
        }

        filterChain.doFilter(request, response);
    }
}
