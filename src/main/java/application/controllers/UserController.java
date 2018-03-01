package application.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import application.models.Session;
import application.models.User;

import javax.servlet.http.HttpSession;

import static application.controllers.messages.Message.*;


@RestController
public class UserController {
    private final Session innerSession;

    public UserController(Session session) {
        this.innerSession = session;
    }

    @PostMapping("/api/join")
    public ResponseEntity<String> signUp(@RequestBody(required = false) User profile) {
        if (profile == null || profile.getLogin() == null || profile.getPassword() == null) {
            return ResponseEntity.badRequest().body(NO_LOGIN_OR_PASSWORD.getMessage());
        }

        if (profile.getLogin().isEmpty() || profile.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(EMPTY_LOGIN_OR_PASSWORD.getMessage());
        }

        final User findedUserByLogin = innerSession.getUserByLogin(profile.getLogin());

        if (findedUserByLogin != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(LOGIN_IS_ALREADY_TAKEN.getMessage());
        }

        innerSession.createUser(profile);
        return ResponseEntity.ok().body(SIGNED_UP.getMessage());
    }

    @PutMapping("/api/user")
    public ResponseEntity<String> updateUser(@RequestBody(required = false) User profile, HttpSession session) {
        if (profile == null || profile.getLogin() == null || profile.getPassword() == null) {
            return ResponseEntity.badRequest().body(NO_LOGIN_OR_PASSWORD.getMessage());
        }

        if (profile.getLogin().isEmpty() || profile.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(EMPTY_LOGIN_OR_PASSWORD.getMessage());
        }

        final String sessionId = session.getId();
        final User findedUserBySessionId = innerSession.getUserBySessionID(sessionId);
        if (findedUserBySessionId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(NOT_AUTHORIZED.getMessage());
        }

        innerSession.updateUser(findedUserBySessionId.getLogin(), profile);
        innerSession.updateSession(sessionId, profile);
        return ResponseEntity.ok().body(USER_PROFILE_UPDATED.getMessage());
    }
}