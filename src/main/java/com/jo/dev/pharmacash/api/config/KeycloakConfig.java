package com.jo.dev.pharmacash.api.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakConfig {

    private final KeycloakProperties adminProps;

    public KeycloakConfig(KeycloakProperties adminProps) {
        this.adminProps = adminProps;
    }

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(adminProps.authServerUrl())
                .realm(adminProps.realm())
                .clientId(adminProps.clientId())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientSecret(adminProps.clientSecret())  // Only if using confidential client
                .build();
    }
}
