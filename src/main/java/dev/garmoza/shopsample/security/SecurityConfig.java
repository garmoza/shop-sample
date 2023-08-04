package dev.garmoza.shopsample.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         CorsConfigurationSource source = request -> {
             CorsConfiguration config = new CorsConfiguration();
             config.applyPermitDefaultValues();
             config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:81"));
             config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
             config.setAllowCredentials(true);

             return config;
         };

         http.cors(corsCustomizer -> corsCustomizer.configurationSource(source));
         http.csrf(AbstractHttpConfigurer::disable);
         http.authorizeHttpRequests(customizer -> customizer.anyRequest().permitAll());

         return http.build();
     }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
