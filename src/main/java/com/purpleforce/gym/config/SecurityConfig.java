package com.purpleforce.gym.config;

import com.purpleforce.gym.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsuarioService usuarioService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/", "/auth/**", "/css/**", "/js/**", "/images/**").permitAll()
                // Solo ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // ADMIN e INSTRUCTOR
                .requestMatchers("/instructor/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                // ADMIN y SOCIO (y cualquier autenticado)
                .requestMatchers("/socio/**").hasAnyRole("ADMIN", "SOCIO")
                // El resto requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler((req, res, auth) -> {
                    // Redirigir según rol tras login
                    var authorities = auth.getAuthorities();
                    String redirect = "/";
                    if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                        redirect = "/admin/dashboard";
                    } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_INSTRUCTOR"))) {
                        redirect = "/instructor/dashboard";
                    } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_SOCIO"))) {
                        redirect = "/socio/dashboard";
                    }
                    res.sendRedirect(redirect);
                })
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(5) // Permite hasta 5 sesiones simultáneas (concurrencia)
                .maxSessionsPreventsLogin(false)
            );

        return http.build();
    }
}
