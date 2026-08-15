package com.jo.dev.pharmacash.api.service.user;

import com.jo.dev.pharmacash.api.dto.UserDto;
import jakarta.ws.rs.core.Response;
import org.jspecify.annotations.NonNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    private final String appRealm;
    private final Keycloak keycloak;


    public UserService(Keycloak keycloak,
                       @Value("${spring.security.oauth2.client.registration.keycloak.provider}") String appRealm) {
        this.keycloak = keycloak;
        this.appRealm = appRealm;
    }

    public void createUser(UserDto userDto) {
        // Define user basic profile
        UserRepresentation user = createUserRepresentation(userDto);

        // Submit the user configuration payload to Keycloak
        try (Response response = keycloak.realm(appRealm).users().create(user)) {
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create user in Keycloak");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while creating user in Keycloak", ex);
        }
    }

    public List<UserRepresentation> getAllUsers() {
        return keycloak.realm(appRealm)
                .users()
                .list();
    }

    public UserRepresentation getUserByUsername(String username) {
        return keycloak.realm(appRealm)
                .users()
                .list()
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public List<UserRepresentation> searchByUserName(String userInfo) {
        return keycloak.realm(appRealm)
                .users()
                .search(userInfo);
    }

    public void deleteUser(String username) {
        keycloak.realm(appRealm).users().list().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .ifPresent(user -> keycloak.realm(appRealm).users().get(user.getId()).remove());
    }

    private static @NonNull UserRepresentation createUserRepresentation(UserDto userDto) {
        UserRepresentation user = new UserRepresentation();
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setEnabled(true);
        user.setEmailVerified(true);

        // Define password credential
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(true); // User will be forced to change it on login
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDto.password());
        user.setCredentials(Collections.singletonList(credential));
        return user;
    }

}
