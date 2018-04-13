package application.controllers;

import application.controllers.requests.ChangeRequest;
import application.controllers.requests.SigninRequest;
import application.controllers.requests.SignupRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import application.models.UserService;
import application.models.User;

import javax.servlet.http.HttpSession;

import static application.controllers.messages.Message.*;


@RestController
public class SessionController {
    private final UserService service;
    public static final String JSON = MediaType.APPLICATION_JSON_UTF8_VALUE;

    public SessionController(UserService service) {
        this.service = service;
    }

    @GetMapping("/api/loginfo")
    public ResponseEntity<Object> getLoggedUser(HttpSession session) {
        final Long sessionId = (Long) session.getAttribute("userId");
        final User findedUserBySessionId = service.getUser(sessionId);
        if (findedUserBySessionId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(NOT_AUTHORIZED.getMessage());
        } else {
            return ResponseEntity.ok(findedUserBySessionId);
        }
    }

    @PostMapping(path = "/api/signup", consumes = JSON, produces = JSON)
    public ResponseEntity<String> signUp(@RequestBody(required = false) SignupRequest profile, HttpSession session) {
        if (profile == null) {
            return ResponseEntity.badRequest().body(NO_LOGIN_OR_PASSWORD.getMessage());
        }

        if (profile.getLogin().isEmpty() || profile.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(EMPTY_LOGIN_OR_PASSWORD.getMessage());
        }

        final User findedUserByLogin = service.getUser(profile.getLogin());

        if (findedUserByLogin != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(LOGIN_IS_ALREADY_TAKEN.getMessage());
        }

        final long id = service.createUser(profile.getLogin(), profile.getPassword(), profile.getEmail());
        session.setAttribute("userId", id);

        return ResponseEntity.ok().body(SIGNED_UP.getMessage());
    }

    @PostMapping(path = "/api/signin", consumes = JSON, produces = JSON)
    public ResponseEntity<String> signIn(@RequestBody(required = false) SigninRequest profile, HttpSession session) {
        if (profile == null) {
            return ResponseEntity.badRequest().body(NO_LOGIN_OR_PASSWORD.getMessage());
        }

        if (profile.getLogin().isEmpty() || profile.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(EMPTY_LOGIN_OR_PASSWORD.getMessage());
        }

        final User user = service.getUser(profile.getLogin());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WRONG_PASSWORD.getMessage());
        }
        final long userId = user.getId();
        if (!service.checkSignin(userId, profile.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WRONG_LOGIN_OR_PASSWORD.getMessage());
        }
        session.setAttribute("userId", userId);
        return ResponseEntity.ok(AUTHORIZED.getMessage());
    }

    @PostMapping(path = "/api/newpassword", consumes = JSON, produces = JSON)
    public ResponseEntity<String> setPassword(@RequestBody(required = false) ChangeRequest body, HttpSession session) {
        final Long sessionId = (Long) session.getAttribute("userId");
        final User currentUser = service.getUser(sessionId);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(NOT_AUTHORIZED.getMessage());
        }

        if (!body.getPassword().equals(currentUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(WRONG_PASSWORD.getMessage());
        }

        if (body.getModifiedString().isEmpty()) {
            return ResponseEntity.badRequest().body(EMPTY_DATA.getMessage());
        }

        service.changePassword(currentUser, body.getModifiedString());
        return ResponseEntity.ok(USER_PROFILE_UPDATED.getMessage());
    }

    @PostMapping(path = "/api/newlogin", consumes = JSON, produces = JSON)
    public ResponseEntity<String> setLogin(@RequestBody(required = false) ChangeRequest body, HttpSession session) {
        final Long sessionId = (Long) session.getAttribute("userId");
        final User currentUser = service.getUser(sessionId);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(NOT_AUTHORIZED.getMessage());
        }

        if (!body.getPassword().equals(currentUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(WRONG_PASSWORD.getMessage());
        }

        if (body.getModifiedString().isEmpty()) {
            return ResponseEntity.badRequest().body(EMPTY_DATA.getMessage());
        }

        service.changeLogin(currentUser, body.getModifiedString());
        return ResponseEntity.ok(USER_PROFILE_UPDATED.getMessage());
    }

    @PostMapping(path = "/api/newemail", consumes = JSON, produces = JSON)
    public ResponseEntity<String> setEmail(@RequestBody(required = false) ChangeRequest body, HttpSession session) {
        final Long sessionId = (Long) session.getAttribute("userId");
        final User currentUser = service.getUser(sessionId);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(NOT_AUTHORIZED.getMessage());
        }

        if (!body.getPassword().equals(currentUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(WRONG_PASSWORD.getMessage());
        }

        if (body.getModifiedString().isEmpty()) {
            return ResponseEntity.badRequest().body(EMPTY_DATA.getMessage());
        }

        service.changeEmail(currentUser, body.getModifiedString());
        return ResponseEntity.ok(USER_PROFILE_UPDATED.getMessage());
    }

    @PostMapping(path = "/api/newavatar", consumes = JSON, produces = JSON)
    public ResponseEntity<String> setAvatar(@RequestBody(required = false) ChangeRequest body, HttpSession session) {
        final Long sessionId = (Long) session.getAttribute("userId");
        final User currentUser = service.getUser(sessionId);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(NOT_AUTHORIZED.getMessage());
        }

        if (!body.getPassword().equals(currentUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(WRONG_PASSWORD.getMessage());
        }

        if (body.getModifiedString().isEmpty()) {
            return ResponseEntity.badRequest().body(EMPTY_DATA.getMessage());
        }

        service.changeAvatar(currentUser, body.getModifiedString());
        return ResponseEntity.ok(USER_PROFILE_UPDATED.getMessage());
    }

    @DeleteMapping("/api/logout")
    public ResponseEntity<String> signOut(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(NOT_AUTHORIZED.getMessage());
        }
        session.removeAttribute("userId");
        return ResponseEntity.status(HttpStatus.OK).body(LOGGED_OUT.getMessage());
    }

    @GetMapping(path = "/api/top", produces = JSON)
    public ResponseEntity getSTopp(@RequestParam(value = "limit", required = false) Integer limit,
                                   @RequestParam(value = "since", required = false) Integer since) {
        if (limit == null) {
            limit = 10;
        }
        if (since == null) {
            since = 0;
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.getTop(limit, since));
    }
}