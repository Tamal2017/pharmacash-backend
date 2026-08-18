package com.jo.dev.pharmacash.api.dto.user;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserInfo {
    String userId;
    String username;
    String email;
    String firstName;
    String lastName;
    String defaultPassword;
    Boolean enabled;
    Map<String, List<String>> attributes;
    List<String> realmRoles;
    Map<String, List<String>> clientRoles; // clientId -> roles
}
