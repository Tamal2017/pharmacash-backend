package com.jo.dev.pharmacash.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain resourceServerSecurityFilterChain(
            HttpSecurity http,
            Converter<Jwt, AbstractAuthenticationToken> authenticationConverter) {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("/", "/h2-console/**").permitAll();
                    requests.requestMatchers("/me").authenticated();
                    requests.anyRequest().denyAll();
                })
                .sessionManagement(sessions ->
                        sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(resourceServer ->
                        resourceServer.jwt(jwtDecoder ->
                                jwtDecoder.jwtAuthenticationConverter(authenticationConverter)
                        )
                );
        // Allow H2 console to be displayed in a frame
        http.headers(headers ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
        );

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter authenticationConverter(AuthoritiesConverter authoritiesConverter) {
        var authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt ->
                authoritiesConverter.convert(jwt.getClaims())
        );
        return authenticationConverter;
    }

    @Bean
    AuthoritiesConverter realmRolesAuthoritiesConverter() {
        return claims -> {
            var realmAccess = claims.get("realm_access");
            if (!(realmAccess instanceof Map<?, ?> realmMap)) {
                return List.of();
            }
            var rolesObj = realmMap.get("roles");
            if (!(rolesObj instanceof List<?> rolesList)) {
                return List.of();
            }
            return rolesList.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .map(GrantedAuthority.class::cast)
                    .toList();
        };
    }

}
