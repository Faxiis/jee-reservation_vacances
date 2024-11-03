package fr.tpreservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
  
    @Bean // Configuration des accès (Authorization)
    SecurityFilterChain filterChain(HttpSecurity http, JwtHeaderFilter jwtHeaderFilter) throws Exception {
        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers(HttpMethod.POST, "/cars/**").hasRole("ADMIN");
            authorize.requestMatchers(HttpMethod.PUT, "/cars/**").hasRole("ADMIN");
            authorize.requestMatchers(HttpMethod.DELETE, "/cars/**").hasRole("ADMIN");

            authorize.requestMatchers(HttpMethod.POST, "/hostels/**").hasRole("ADMIN");
            authorize.requestMatchers(HttpMethod.PUT, "/hostels/**").hasRole("ADMIN");
            authorize.requestMatchers(HttpMethod.DELETE, "/hostels/**").hasRole("ADMIN");

            authorize.requestMatchers(HttpMethod.POST, "/flights/**").hasRole("ADMIN");

            authorize.requestMatchers(HttpMethod.POST, "/parkings/**").hasRole("ADMIN");

            authorize.requestMatchers("users/subscribe", "/users/auth").permitAll();


            authorize.requestMatchers("/**").authenticated();
        });
        http.csrf(csrf -> csrf.disable());
        // http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        http.addFilterBefore(jwtHeaderFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean // Configuration de l'encodeur de mot de passe
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
