package com.jo.dev.pharmacash.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds properties under keycloak.admin-cli in application.yaml
 */
@ConfigurationProperties(prefix = "keycloak.admin-api")
public record KeycloakProperties(
        String authServerUrl,
        String realm,
        String clientId,
        String clientSecret) {
}

