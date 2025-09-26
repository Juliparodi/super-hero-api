package com.superhero.config.security;

import com.superhero.config.security.filters.CustomJwtAuthenticationFilter;
import com.superhero.config.security.filters.JwtTokenValidationFilter;
import com.superhero.utils.JwtUtilsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.superhero.constants.SecurityConstants.ROLE_ADMIN;
import static com.superhero.constants.SecurityConstants.ROLE_READ;

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
    @Order(1)
    SecurityFilterChain actuatorSecurity(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/actuator/**")
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }


    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(
        HttpSecurity http) throws Exception {

        http
            .sessionManagement(sessionManagement ->
                sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(requests-> requests
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/swagger-ui.html/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/swagger-ui.html#/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/v3/api-docs/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui.html")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui.html#")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher( "/h2-console/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher((HttpMethod.GET), "/superheroes")).hasAnyRole(ROLE_ADMIN, ROLE_READ)
                .requestMatchers(AntPathRequestMatcher.antMatcher((HttpMethod.GET), "/search/**")).hasAnyRole(ROLE_ADMIN, ROLE_READ)
                .requestMatchers(AntPathRequestMatcher.antMatcher((HttpMethod.GET), "/superheroes/**")).hasAnyRole(ROLE_ADMIN, ROLE_READ)
                .requestMatchers(AntPathRequestMatcher.antMatcher((HttpMethod.POST), "/superheroes/hero")).hasRole(ROLE_ADMIN)
                .requestMatchers(AntPathRequestMatcher.antMatcher((HttpMethod.PUT), "/superheroes/hero/**")).hasRole(ROLE_ADMIN)
                .requestMatchers(AntPathRequestMatcher.antMatcher((HttpMethod.DELETE), "/superheroes/hero/**")).hasRole(ROLE_ADMIN)
                .anyRequest().authenticated())
            .addFilterBefore(new JwtTokenValidationFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new CustomJwtAuthenticationFilter(authenticationManager(), jwtUtils), UsernamePasswordAuthenticationFilter.class)
            .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000",
            "https://public-superhero-juliparodi19-dev.apps.rm1.0a51.p1.openshiftapps.com"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder.encode(adminPassword))
            .roles("admin")
            .build();

        UserDetails endUser = User.withUsername("user")
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
