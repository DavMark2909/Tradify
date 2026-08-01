package com.tradify.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class FilterChain {

    private final static List<String> allowedOrigins = Arrays.asList("*", "*");
    private final static List<String> allowedMethods = Arrays.asList("*", "*");

    @Bean
    public SecurityFilterChain asFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer.oidc(Customizer.withDefaults()));

        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

        http.csrf(c -> c.disable());

        http.cors(c -> {
            CorsConfigurationSource source = request -> {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(allowedOrigins);
                configuration.setAllowedMethods(allowedMethods);
                configuration.setAllowedHeaders(List.of("*"));
                return configuration;
            };
            c.configurationSource(source);
        });

        return http.build();
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register/**", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
//create an API endpoint that is being listened by spring, when user logs in, this API is triggered
                        .loginProcessingUrl("/perform_login")
                        .permitAll()
                );
        return http.build();
    }

}
