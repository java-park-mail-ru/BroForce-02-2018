package application.controller;

import application.services.AccountService;
import application.utils.Messages;
import application.utils.Validator;
import application.utils.requests.SignupRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class ControllerTest {
    private static final String LOGIN = "testu";
    private static final String PASSWORD = "qwerty123";
    private static final String EMAIL = "testu@mail.ru";
    private static final long BADID = 34348L;
    private long userId;

    @Autowired
    AccountService service;

    @Autowired
    MockMvc mock;

    @Before
    public void before() {
        userId = service.addUser(new SignupRequest(LOGIN, PASSWORD, EMAIL));
    }

    @After
    public void after() {
        service.clear();
    }

    @Test
    public void signup() throws Exception {
        mock.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"testu\", \"password\":\"qwerty123\", \"email\":\"testu@mail.ru\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(String.valueOf(userId + 1)))
                .andExpect(jsonPath("login").value("testu"))
                .andExpect(jsonPath("email").value("testu@mail.ru"));
    }

    @Test
    public void authorizedSignup() throws Exception {
        mock.perform(post("/api/signup")
                .sessionAttr("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"testu\", \"password\":\"qwerty123\", \"email\":\"testu@mail.ru\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("message").value(Messages.AUTHORIZED));
    }

    @Test
    public void conflictSignup() throws Exception {
        mock.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"vitalya\", \"password\":\"qwerty123\", \"email\":\"testu@mail.ru\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value(Messages.EXISTS));

        mock.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"testu\", \"password\":\"qwerty123\", \"email\":\"vitalya@mail.ru\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value(Messages.EXISTS));

        mock.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"vitalya\", \"password\":\"qwerty123\", \"email\":\"vitalya@mail.ru\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value(Messages.EXISTS));
    }

    @Test
    public void invalidSignup() throws Exception {
        final ArrayList<String> error = new ArrayList<>();
        error.add(Validator.SHORT_LOGIN);
        error.add(Validator.SHORT_PASSWORD);
        error.add(Validator.EMPTY_EMAIL);
        mock.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"tes\", \"password\":\"qwe\", \"email\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(error));

        error.clear();
        error.add(Validator.LONG_LOGIN);
        error.add(Validator.LONG_PASSWORD);
        error.add(Validator.EMAIL_ERROR);
        mock.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"testuujvnhjgnvvhgnvhghvnhg3\", \"password\":\"ufhdgnvbchftfagdbcvfgcvfvfvv\", \"email\":\"testumail.ru\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(error));

        error.clear();
        error.add(Validator.LOGIN_ERROR);
        error.add(Validator.EMPTY_PASSWORD);
        error.add(Validator.EMAIL_ERROR);
        mock.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"вннввсакмкмк\", \"password\":\"\", \"email\":\"testumail.ru\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(error));
    }

    @Test
    public void signin() throws Exception {
        mock.perform(post("/api/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"vitalya\", \"password\":\"qwerty123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(String.valueOf(userId)))
                .andExpect(jsonPath("login").value("vitalya"))
                .andExpect(jsonPath("email").value("vitalya@mail.ru"));
    }

    @Test
    public void authorizedSignin() throws Exception {
        mock.perform(post("/api/signin")
                .sessionAttr("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"vitalya\", \"password\":\"qwerty123\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("message").value(Messages.AUTHORIZED));
    }

    @Test
    public void wrongSignin() throws Exception {
        mock.perform(post("/api/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"jrhvbghjrv\", \"password\":\"qwerty123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(Messages.WRONG_LOGIN_PASSWORD));

        mock.perform(post("/api/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"vitalya\", \"password\":\"qwefvty123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(Messages.WRONG_LOGIN_PASSWORD));

        mock.perform(post("/api/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"idfvdfa12\", \"password\":\"qwertdfvdy123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(Messages.WRONG_LOGIN_PASSWORD));
    }

    @Test
    public void unauthorizedChangePassword() throws Exception {
        mock.perform(post("/api/newpassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwerty123\", \"change\":\"qwerty1234\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value(Messages.NOT_AUTHORIZE));
    }

    @Test
    public void unauthorizedChangeEmail() throws Exception {
        mock.perform(post("/api/newemail")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwerty123\", \"change\":\"qwerty@mail.ru\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value(Messages.NOT_AUTHORIZE));
    }

    @Test
    public void unauthorizedChangeLogin() throws Exception {
        mock.perform(post("/api/newlogin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwerty123\", \"change\":\"qwerty\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value(Messages.NOT_AUTHORIZE));
    }

    @Test
    public void changePassword() throws Exception {
        mock.perform(post("/api/newpassword")
                .sessionAttr("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwerty123\", \"change\":\"qwerty1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(String.valueOf(userId)))
                .andExpect(jsonPath("login").value("ilya"))
                .andExpect(jsonPath("email").value("ilya@mail.ru"));
    }

    @Test
    public void changeEmail() throws Exception {
        mock.perform(post("/newemail")
                .sessionAttr("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwerty123\", \"change\":\"qwerty@mail.ru\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(String.valueOf(userId)))
                .andExpect(jsonPath("login").value("ilya"))
                .andExpect(jsonPath("email").value("qwerty@mail.ru"));
    }

    @Test
    public void changeLogin() throws Exception {
        mock.perform(post("/api/newlogin")
                .sessionAttr("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwerty123\", \"change\":\"qwerty\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(String.valueOf(userId)))
                .andExpect(jsonPath("login").value("qwerty"))
                .andExpect(jsonPath("email").value("ilya@mail.ru"));
    }

    @Test
    public void changePasswordBadCookie() throws Exception {
        mock.perform(post("/api/newpassword")
                .sessionAttr("userId", BADID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwerty123\", \"change\":\"qwerty1234\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("message").value(Messages.BAD_COOKIE));
    }

    @Test
    public void changeEmailBadCookie() throws Exception {
        mock.perform(post("/api/newemail")
                .sessionAttr("userId", BADID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwerty123\", \"change\":\"qwerty@mail.ru\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("message").value(Messages.BAD_COOKIE));
    }

    @Test
    public void changeLoginBadCookie() throws Exception {
        mock.perform(post("/api/newlogin")
                .sessionAttr("userId", BADID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwerty123\", \"change\":\"qwerty\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("message").value(Messages.BAD_COOKIE));
    }

    @Test
    public void changeLoginWrongPassword() throws Exception {
        mock.perform(post("/api/newlogin")
                .sessionAttr("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwer1ty123\", \"change\":\"qwerty\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(Messages.WRONG_PASSWORD));
    }

    @Test
    public void changePasswordWrongPassword() throws Exception {
        mock.perform(post("/api/newpassword")
                .sessionAttr("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwe1rty123\", \"change\":\"qwerty1234\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(Messages.WRONG_PASSWORD));
    }

    @Test
    public void changeEmailWrongPassword() throws Exception {
        mock.perform(post("/api/newemail")
                .sessionAttr("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwert1y123\", \"change\":\"qwerty@mail.ru\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(Messages.WRONG_PASSWORD));
    }

    @Test
    public void changeLoginConflict() throws Exception {
        mock.perform(post("/api/newlogin")
                .sessionAttr("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwerty123\", \"change\":\"ilya\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value(Messages.LOGIN_EXISTS));
    }

    @Test
    public void changeEmailConflict() throws Exception {
        mock.perform(post("/api/newemail")
                .sessionAttr("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"qwerty123\", \"change\":\"ilya@mail.ru\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value(Messages.EMAIL_EXISTS));
    }

    @Test
    public void unauthorizedLogout() throws Exception {
        mock.perform(post("/api/logout"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value(Messages.NOT_AUTHORIZE));
    }

    @Test
    public void logout() throws Exception {
        mock.perform(post("/api/logout")
                .sessionAttr("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value(Messages.SUCCESS));
    }

    @Test
    public void getUser() throws Exception {
        mock.perform(get("/api/loginfo")
                .sessionAttr("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(String.valueOf(userId)))
                .andExpect(jsonPath("login").value(LOGIN))
                .andExpect(jsonPath("email").value(EMAIL));
    }

    @Test
    public void unauthorizedGetUser() throws Exception {
        mock.perform(get("/api/loginfo"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value(Messages.NOT_AUTHORIZE));
    }

    @Test
    public void getUserWithBadCookies() throws Exception {
        mock.perform(get("/api/loginfo")
                .sessionAttr("userId", BADID))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("message").value(Messages.BAD_COOKIE));
    }

}
