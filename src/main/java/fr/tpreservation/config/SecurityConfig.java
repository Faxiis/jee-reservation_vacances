package fr.tpreservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
            // authorize.requestMatchers("/api/fournisseur/**").hasAuthority("ROLE_ADMIN");
            authorize.requestMatchers("/api/fournisseur/**").hasRole("ADMIN");
            authorize.requestMatchers("users/subscribe", "/users/auth", "cars/").permitAll();
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
    // @Bean // Configuration des users (Authentication)
    // UserDetailsService inMemory(PasswordEncoder passwordEncoder) {
    //     InMemoryUserDetailsManager inMemory = new InMemoryUserDetailsManager();
    //     inMemory.createUser(
    //         User.withUsername("user")
    //             // .password("{noop}123456")
    //             .password(passwordEncoder.encode("123456"))
    //             // .authorities("ROLE_USER")
    //             .roles("USER") // C'est pareil que ROLE_USER
    //             .build()
    //     );
    //     inMemory.createUser(
    //         User.withUsername("admin")
    //             .password(passwordEncoder.encode("123456$"))
    //             // .authorities("ROLE_ADMIN")
    //             .roles("ADMIN") // C'est pareil que ROLE_USER
    //             .build()
    //     );
    //     return inMemory;
    // }
}
