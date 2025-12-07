package com.usermodule.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.usermodule.dto.user.UserDetailsDTO;
import com.usermodule.service.security.JwtService;
import com.usermodule.utils.AuthUtil;
import com.usermodule.utils.HttpUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthUtil authUtil;
    private final HttpUtil httpUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (CorsUtils.isPreFlightRequest(request)) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        try {
            String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            var token = jwtService.resolveToken(bearerToken);
            if (token != null) {
                UserDetails userDetails = jwtService.tokenValidate(token);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken auth = jwtService.getAuthentication(userDetails);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    var user = ((UserDetailsDTO)auth.getPrincipal()).getUser();
                    authUtil.setUser(user.getUserId(), user.getUsername(), httpUtil.getHostName(),httpUtil.getHostAddress(),
                            httpUtil.getRequestIp(request));
                }
            } else {
                request.setAttribute("businessExceptionMsg", "The Token is Null");
            }
        } catch (TokenExpiredException ex) {
            SecurityContextHolder.clearContext();
            response.getWriter().write(ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            request.setAttribute("businessExceptionMsg", ex.getMessage());
        } catch (RuntimeException ex) {
            SecurityContextHolder.clearContext();
            response.getWriter().write(ex.getMessage());
            request.setAttribute("businessExceptionMsg", ex.getMessage());
            throw ex;
        }

        filterChain.doFilter(request, response);
    }

}
