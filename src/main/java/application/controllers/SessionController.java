package application.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import application.models.Session;
import application.models.User;

import javax.servlet.http.HttpSession;

import static application.controllers.messages.Message.*;


@RestController
public class SessionController {
    private final Session innerSession;

    public SessionController(Session session) {
        this.innerSession = session;
    }

    @GetMapping("/api/auth")
    public ResponseEntity getLoggedUser(HttpSession session) {
        final String sessionId = session.getId();
        final User findedUserBySessionId = innerSession.getUserBySessionID(sessionId);
        if (findedUserBySessionId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(NOT_AUTHORIZED.getMessage());
        } else {
            return ResponseEntity.ok(findedUserBySessionId);
        }
    }

    @PostMapping("/api/auth")
    public ResponseEntity<String> signIn(@RequestBody(required = false) User profile, HttpSession session) {
        if (profile == null || profile.getLogin() == null || profile.getPassword() == null) {
            return ResponseEntity.badRequest().body(NO_LOGIN_OR_PASSWORD.getMessage());
        }

        if (profile.getLogin().isEmpty() || profile.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(EMPTY_LOGIN_OR_PASSWORD.getMessage());
        }

        final User findedUserByLogin = innerSession.getUserByLogin(profile.getLogin());
        if (findedUserByLogin == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(WRONG_LOGIN_OR_PASSWORD.getMessage());
        }

        final String sessionId = session.getId();
        final User findedUserBySessionId = innerSession.getUserBySessionID(sessionId);
        if (findedUserBySessionId != null) {
            if (findedUserBySessionId.equals(findedUserByLogin)) {
                return ResponseEntity.ok().body(ALREADY_AUTHORIZED.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ANOTHER_ALREADY_AUTHORIZED.getMessage());
            }
        }

        if (!findedUserByLogin.getPassword().equals(profile.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(WRONG_LOGIN_OR_PASSWORD.getMessage());
        }

        innerSession.createSession(sessionId, findedUserByLogin);
        return ResponseEntity.ok().body(AUTHORIZED.getMessage());
    }

    @DeleteMapping("/api/auth")
    public ResponseEntity<String> signOut(HttpSession session) {
        final String sessionId = session.getId();
        final User findedUserBySessionId = innerSession.getUserBySessionID(sessionId);

        if (findedUserBySessionId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(NOT_AUTHORIZED.getMessage());
        }

        innerSession.deleteSession(sessionId);
        return ResponseEntity.ok().body(LOGGED_OUT.getMessage());
    }
}