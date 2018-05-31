package application.controllers;

import application.controllers.requests.ChangeRequest;
import application.controllers.requests.SigninRequest;
import application.controllers.requests.SignupRequest;
import application.controllers.responses.ErrorResponse;
import application.controllers.responses.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import application.models.UserService;
import application.models.User;

import javax.servlet.http.HttpSession;

import static application.controllers.messages.Message.*;


@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
public class SessionController {
    
    @NotNull
    private final UserService service;
    public static final String JSON = MediaType.APPLICATION_JSON_UTF8_VALUE;

    public SessionController(@NotNull UserService service) {
        this.service = service;
    }

    @GetMapping("/api/loginfo")
    public ResponseEntity<Object> getLoggedUser(HttpSession session) {
        final Long id = (Long) session.getAttribute("userId");
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(WRONG_ID));
        }

        final User user = service.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(WRONG_ID));
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping(path = "/api/signup", consumes = JSON, produces = JSON)
    public ResponseEntity<Object> signUp(@RequestBody(required = false) SignupRequest profile, HttpSession session) {
        if (session.getAttribute("userId") != null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(ALREADY_AUTHORIZED));
        }

        if (profile == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(NO_LOGIN_OR_PASSWORD));
        }

        if (profile.getLogin().isEmpty() || profile.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(EMPTY_LOGIN_OR_PASSWORD));
        }

        if (!service.checkLogin(profile.getLogin())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(LOGIN_IS_ALREADY_TAKEN));
        }

        if (!service.checkEmail(profile.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(EMAIL_IS_ALREADY_TAKEN));
        }

        final long id = service.createUser(profile.getLogin(), profile.getPassword(), profile.getEmail());
        session.setAttribute("userId", id);

        return ResponseEntity.ok().body(new MessageResponse(SIGNED_UP));
    }

    @PostMapping(path = "/api/signin", consumes = JSON, produces = JSON)
    public ResponseEntity<Object> signIn(@RequestBody(required = false) SigninRequest profile, HttpSession session) {
        if (session.getAttribute("userId") != null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(ALREADY_AUTHORIZED));
        }

        if (profile == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(NO_LOGIN_OR_PASSWORD));
        }

        if (profile.getLogin().isEmpty() || profile.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(EMPTY_LOGIN_OR_PASSWORD));
        }

        final User user = service.getUser(profile.getLogin());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(WRONG_LOGIN));
        }

        final long userId = user.getId();
        if (!service.checkSignin(userId, profile.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(WRONG_PASSWORD));
        }

        session.setAttribute("userId", userId);
        return ResponseEntity.ok(new MessageResponse(AUTHORIZED));
    }

    @PostMapping(path = "/api/newpassword", consumes = JSON, produces = JSON)
    public ResponseEntity<Object> setPassword(@RequestBody(required = false) ChangeRequest body, HttpSession session) {
        final Long id = (Long) session.getAttribute("userId");
        if (id == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(NOT_AUTHORIZED));
        }

        if (body == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(EMPTY_DATA));
        }

        if (body.getModifiedString().isEmpty() || body.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(EMPTY_DATA));
        }

        final User user = service.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(WRONG_ID));
        }

        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(WRONG_PASSWORD));
        }

        service.changePassword(user, body.getModifiedString());
        return ResponseEntity.ok(new MessageResponse(USER_PROFILE_UPDATED));
    }

    @PostMapping(path = "/api/newlogin", consumes = JSON, produces = JSON)
    public ResponseEntity<Object> setLogin(@RequestBody(required = false) ChangeRequest body, HttpSession session) {
        final Long id = (Long) session.getAttribute("userId");
        if (id == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(NOT_AUTHORIZED));
        }

        if (body == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(EMPTY_DATA));
        }

        if (body.getModifiedString().isEmpty() || body.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(EMPTY_DATA));
        }

        final User user = service.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(WRONG_ID));
        }

        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(WRONG_PASSWORD));
        }

        if (!service.checkLogin(body.getModifiedString())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(LOGIN_IS_ALREADY_TAKEN));
        }

        service.changeLogin(user, body.getModifiedString());
        return ResponseEntity.ok(new MessageResponse(USER_PROFILE_UPDATED));
    }

    @PostMapping(path = "/api/newemail", consumes = JSON, produces = JSON)
    public ResponseEntity<Object> setEmail(@RequestBody(required = false) ChangeRequest body, HttpSession session) {
        final Long id = (Long) session.getAttribute("userId");
        if (id == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(NOT_AUTHORIZED));
        }

        if (body == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(EMPTY_DATA));
        }

        if (body.getModifiedString().isEmpty() || body.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(EMPTY_DATA));
        }

        final User user = service.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(WRONG_ID));
        }

        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(WRONG_PASSWORD));
        }

        if (!service.checkEmail(body.getModifiedString())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(EMAIL_IS_ALREADY_TAKEN));
        }

        service.changeEmail(user, body.getModifiedString());
        return ResponseEntity.ok(new MessageResponse(USER_PROFILE_UPDATED));
    }

    @PostMapping(path = "/api/newavatar", consumes = JSON, produces = JSON)
    public ResponseEntity<Object> setAvatar(@RequestBody(required = false) ChangeRequest body, HttpSession session) {
        final Long id = (Long) session.getAttribute("userId");
        if (id == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(NOT_AUTHORIZED));
        }

        if (body == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(EMPTY_DATA));
        }

        if (body.getModifiedString().isEmpty() || body.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(EMPTY_DATA));
        }

        final User user = service.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(WRONG_ID));
        }

        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(WRONG_PASSWORD));
        }

        service.changeAvatar(user, body.getModifiedString());
        return ResponseEntity.ok(new MessageResponse(USER_PROFILE_UPDATED));
    }

    @PostMapping(path = "/api/win", consumes = JSON, produces = JSON)
    public ResponseEntity<Object> updateS(HttpSession session) {
        final Long id = (Long) session.getAttribute("userId");
        if (id == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(NOT_AUTHORIZED));
        }

        final User user = service.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(WRONG_ID));
        }

        service.updateScoreS(id);
        return ResponseEntity.ok(new MessageResponse(USER_PROFILE_UPDATED));
    }

    @PostMapping(path = "/api/logout", produces = JSON)
    public ResponseEntity logout(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(NOT_AUTHORIZED));
        }

        session.removeAttribute("userId");
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(LOGGED_OUT));
    }

    @DeleteMapping("/api/clear")
    public ResponseEntity<Object> clear(HttpSession session) {

        final Long id = (Long) session.getAttribute("userId");
        if (id != null) {

            final User user = service.getUser(id);
            if (user != null && user.getLogin().equals("root")) {
                service.clear();
                session.removeAttribute("userId");
                return ResponseEntity.status(HttpStatus.OK).body("CLEARED");
            }

        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(WRONG_LOGIN));
    }

    
    @GetMapping(path = "/api/tops", produces = JSON)
    public ResponseEntity getTopS(@RequestParam(value = "limit", required = false) Integer limit,
                                  @RequestParam(value = "since", required = false) Integer since) {
        if (limit == null) {
            limit = 10;
        }
        if (since == null) {
            since = 0;
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.getTopS(limit, since));
    }

    @GetMapping(path = "/api/topm", produces = JSON)
    public ResponseEntity getTopM(@RequestParam(value = "limit", required = false) Integer limit,
                                  @RequestParam(value = "since", required = false) Integer since) {
        if (limit == null) {
            limit = 10;
        }
        if (since == null) {
            since = 0;
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.getTopM(limit, since));
    }
}
