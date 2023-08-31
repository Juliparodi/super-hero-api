package com.superhero.config.security;

import static com.superhero.constants.SecurityConstants.ROLE_ADMIN;
import static com.superhero.constants.SecurityConstants.ROLE_READ;

import com.superhero.config.security.filters.CustomJwtAuthenticationFilter;
import com.superhero.config.security.filters.JwtTokenValidationFilter;
import com.superhero.utils.JwtUtilsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${custom.admin.password}")
    private String adminPassword;

    @Value("${custom.endUser.password}")
    private String endUserPassword;

    private JwtUtilsWrapper jwtUtils;

    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtilsWrapper jwtUtils) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(
        HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http
            .sessionManagement(sessionManagement ->
                sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(requests->requests
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/swagger-ui.html/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/v3/api-docs/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui.html")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher( "/h2-console/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher((HttpMethod.GET), "/superheroes")).hasAnyRole(ROLE_ADMIN, ROLE_READ)
                .requestMatchers(AntPathRequestMatcher.antMatcher((HttpMethod.GET), "/search/**")).hasAnyRole(ROLE_ADMIN, ROLE_READ)
                .requestMatchers(AntPathRequestMatcher.antMatcher((HttpMethod.GET), "/superheroes/**")).hasAnyRole(ROLE_ADMIN, ROLE_READ)
                .requestMatchers(AntPathRequestMatcher.antMatcher((HttpMethod.POST), "/superheroes/hero")).hasRole(ROLE_ADMIN)
                .requestMatchers(AntPathRequestMatcher.antMatcher((HttpMethod.PUT), "/superheroes/hero/**")).hasRole(ROLE_ADMIN)
                .requestMatchers(AntPathRequestMatcher.antMatcher((HttpMethod.DELETE), "/superheroes/hero/**")).hasRole(ROLE_ADMIN)
                .anyRequest().authenticated())
           //.addFilterBefore(new JwtTokenValidationFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
            //.addFilterBefore(new CustomJwtAuthenticationFilter(authenticationManager(), jwtUtils), UsernamePasswordAuthenticationFilter.class)
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder.encode(adminPassword))
            .roles("admin")
            .build();

        UserDetails endUser = User.withUsername("endUser")
            .password(passwordEncoder.encode(endUserPassword))
            .roles("read")
            .build();

        return new InMemoryUserDetailsManager(admin,endUser);
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
