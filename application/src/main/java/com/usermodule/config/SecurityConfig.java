package com.usermodule.config;

import com.usermodule.exception.AccessDeniedHandlerException;
import com.usermodule.exception.AuthenticationEntryPointException;
import com.usermodule.filter.JwtFilter;
import com.usermodule.service.security.PasswordEncoderService;
import com.usermodule.service.security.UserDetailsImplService;
import com.usermodule.utils.CustomPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsImplService userDetailsService;
    private final AccessDeniedHandlerException accessDeniedHandlerException;
    private final AuthenticationEntryPointException authenticationEntryPointException;
    private final PasswordEncoderService passwordEncoderService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder(passwordEncoderService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(mvcMatcherBuilder.pattern("/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/auth/login")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/user/forgetPassword")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/user/changePassword")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/dashboard/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/app/server/manage/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandlerException))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPointException))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
