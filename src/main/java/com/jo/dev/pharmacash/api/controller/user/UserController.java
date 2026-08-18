package com.jo.dev.pharmacash.api.controller.user;

import com.jo.dev.pharmacash.api.dto.user.UserInfo;
import com.jo.dev.pharmacash.api.service.user.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserInfoDto getConnectedUserInfo(JwtAuthenticationToken auth) {
        return new UserInfoDto(
                auth.getToken().getClaimAsString(StandardClaimNames.PREFERRED_USERNAME),
                auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    }

    @GetMapping("/")
    public List<UserRepresentation> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public UserRepresentation getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/search")
    public List<UserRepresentation> searchUserByUsername(@RequestParam("userInfo") String userInfo) {
        return userService.searchUserByUsername(userInfo);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestBody UserInfo userInfo) {
        userService.createUser(userInfo);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> updateUser(@RequestBody UserInfo userInfo) {
        userService.updateUser(userInfo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    public record UserInfoDto(String name, List<String> roles) {
    }
}
