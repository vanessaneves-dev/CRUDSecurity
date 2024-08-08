package com.example.CRUD.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/pessoa/cadastrar", "/pessoa/save").hasRole("ADMIN") // Permissão para ADMIN
                        .requestMatchers("/pessoa/listar", "/pessoa/alterar/**", "/pessoa/excluir/**").hasAnyRole("ADMIN", "USER") // Permissão para ADMIN e USER
                        .anyRequest().authenticated() // Qualquer outra solicitação precisa estar autenticada
                )
                .formLogin((form) -> form
                        .loginPage("/login").permitAll() // Configura a página de login
                )
                .logout((logout) -> logout.permitAll()); // Permite logout para todos
        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()); // Configura o serviço de detalhes do usuário e o codificador de senhas
    }
}


