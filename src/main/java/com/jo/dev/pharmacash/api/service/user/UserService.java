package com.jo.dev.pharmacash.api.service.user;

import com.jo.dev.pharmacash.api.dto.user.UserInfo;
import com.jo.dev.pharmacash.api.exception.UserException;
import jakarta.ws.rs.core.Response;
import org.jspecify.annotations.NonNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final String defaultPassword;
    private final Keycloak keycloak;
    private final String appRealm;

    public UserService(@Value("${keycloak.user.defaultPassword}") String defaultPassword, Keycloak keycloak,
                       @Value("${spring.security.oauth2.client.registration.keycloak.provider}") String appRealm) {
        this.defaultPassword = defaultPassword;
        this.keycloak = keycloak;
        this.appRealm = appRealm;
    }

    public void createUser(UserInfo userInfo) {
        // Define user basic profile
        UserRepresentation user = createUserRepresentation(userInfo);

        // Submit the user configuration payload to Keycloak
        try (Response response = keycloak.realm(appRealm).users().create(user)) {
            if (response.getStatus() != 201) {
                throw new UserException("Failed to create user in Keycloak");
            }
        } catch (Exception ex) {
            throw new UserException("Error occurred while creating user in Keycloak", ex);
        }
    }

    public void updateUser(UserInfo userInfo) {
        UserRepresentation existingUser = getUserByUsername(userInfo.getUsername());
        if (existingUser == null) {
            throw new UserException("UserInfo not found with username: " + userInfo.getUsername());
        }

        RealmResource realm = keycloak.realm(appRealm);
        UserResource userResource = realm.users().get(existingUser.getId());

        // --- Update user fields ---
        UserRepresentation user = userResource.toRepresentation();

        if (StringUtils.hasText(user.getUsername())) {
            user.setUsername(userInfo.getUsername());
        }
        if (StringUtils.hasText(userInfo.getEmail())) {
            user.setEmail(userInfo.getEmail());
        }
        if (userInfo.getFirstName() != null) {
            user.setFirstName(user.getFirstName());
        }
        if (userInfo.getLastName() != null) {
            user.setLastName(userInfo.getLastName());
        }
        if (userInfo.getEnabled() != null) {
            user.setEnabled(userInfo.getEnabled());
        }
        if (!CollectionUtils.isEmpty(userInfo.getAttributes())) {
            user.setAttributes(userInfo.getAttributes());
        }

        userResource.update(user);

        // --- Assign Realm Roles ---
        if (!CollectionUtils.isEmpty(userInfo.getRealmRoles())) {
            List<RoleRepresentation> roles = userInfo.getRealmRoles().stream()
                    .map(roleName -> realm.roles().get(roleName).toRepresentation())
                    .toList();

            userResource.roles().realmLevel().add(roles);
        }

        // --- Assign Client Roles ---

        if (!CollectionUtils.isEmpty(userInfo.getClientRoles())) {

            for (Map.Entry<String, List<String>> entry : userInfo.getClientRoles().entrySet()) {
                String clientId = entry.getKey();
                List<String> roles = entry.getValue();

                // Get client UUID
                ClientRepresentation client = realm.clients()
                        .findByClientId(clientId)
                        .get(0);

                List<RoleRepresentation> clientRoleReps = roles.stream()
                        .map(roleName -> realm.clients()
                                .get(client.getId())
                                .roles()
                                .get(roleName)
                                .toRepresentation())
                        .toList();

                userResource.roles()
                        .clientLevel(client.getId())
                        .add(clientRoleReps);
            }
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
                .orElseThrow(() -> new RuntimeException("UserInfo not found with username: " + username));
    }

    public List<UserRepresentation> searchUserByUsername(String userInfo) {
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

    private @NonNull UserRepresentation createUserRepresentation(UserInfo userInfo) {
        UserRepresentation user = new UserRepresentation();
        user.setFirstName(userInfo.getFirstName());
        user.setLastName(userInfo.getLastName());
        user.setUsername(userInfo.getUsername());
        user.setEmail(userInfo.getEmail());
        user.setEnabled(true);
        user.setEmailVerified(true);

        // Define password credential
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(true); // UserInfo will be forced to change it on login
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(StringUtils.hasText(userInfo.getDefaultPassword()) ? userInfo.getDefaultPassword() : defaultPassword);
        user.setCredentials(Collections.singletonList(credential));
        return user;
    }

}
