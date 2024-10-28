package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                //.csrf().disable() //Cross-site request forgery -- inhabilita seguridad de spring security por defecto, afectaría a formularios si está deshabilitado
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v1/index2")
                        .permitAll()
                        .anyRequest().authenticated()
                ) //se puede configurar URLs que están protegidas
                //.requestMatchers("/v1/index2") //peticiones que coincidan con endpoints que se definen aquí, son peticiones que no necesitarán autorizaciones
                //.permitAll()
                //.anyRequest().authenticated()
                //.and()
                .formLogin(formLogin -> formLogin
                        .successHandler(successHandler())
                        .permitAll()
                )
                .sessionManagement(session ->
                        session
                                .sessionFixation().newSession() //Crea una sesión completamente nueva
                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                .invalidSessionUrl("/login")
                                .maximumSessions(1)
                                .expiredUrl("/login") //Si el usuario tiene un tiempo de inactividad y se cumple el tiempo, se redirigirá a ...
                                .sessionRegistry(sessionRegistry()) //Habilitar rastreo de datos del usuario
                )
                .build();
    }

    public AuthenticationSuccessHandler successHandler(){
        return ((request, response, authentication) -> {
            response.sendRedirect("/v1/session");}
        );
    }

   @Bean
    public SessionRegistry sessionRegistry(){//Objeto que se encarga de administrar todos los registros que estén en la sesión
        return new SessionRegistryImpl();
    }
}
