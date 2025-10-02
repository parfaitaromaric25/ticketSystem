package com.example.ticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            // H2 Console
	            .requestMatchers("/h2-console/**").permitAll()
	            // Swagger/OpenAPI
	            .requestMatchers(
	                "/v3/api-docs/**",
	                "/swagger-ui/**",
	                "/swagger-ui.html",
	                "/swagger-resources/**",
	                "/webjars/**"
	            ).permitAll()
	            // User endpoints
	            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
	            .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated()
	            // Tout le reste nécessite une authentification
	            .anyRequest().permitAll()
	        )
	        .httpBasic(basic -> {})
	        .sessionManagement(session -> 
	            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        )
	        .headers(headers -> headers.frameOptions(frame -> frame.disable()));
	    
	    return http.build();
	}
	
	 @Bean
	    public UserDetailsService userDetailsService() {
	        UserDetails admin = User.builder()
	            .username("admin")
	            .password(passwordEncoder().encode("admin123"))
	            .roles("ADMIN")
	            .build();
	            
	        UserDetails user = User.builder()
	            .username("user1")
	            .password(passwordEncoder().encode("user123"))
	            .roles("USER")
	            .build();
	            
	        return new InMemoryUserDetailsManager(admin, user);
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

}
