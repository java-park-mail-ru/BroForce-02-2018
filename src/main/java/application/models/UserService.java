package application.models;

import application.database.DAOi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @NotNull
    private final DAOi db;
    @NotNull
    private final PasswordEncoder encoder;

    public UserService(@NotNull DAOi db, @NotNull PasswordEncoder encoder) {
        this.db = db;
        this.encoder = encoder;
    }

    @NotNull
    public Long createUser(String login, String password, String email) {
        final String encodedPassword = encoder.encode(password);
        return db.addUser(login, encodedPassword, email);
    }

    public void changePassword(@NotNull User user, @NotNull String password) {
        password = encoder.encode(password);
        user.setPassword(password);
        db.changeUserData(user);
    }

    public void changeLogin(@NotNull User user, @NotNull String login) {
        user.setLogin(login);
        db.changeUserData(user);
    }

    public void changeEmail(@NotNull User user, @NotNull String email) {
        user.setEmail(email);
        db.changeUserData(user);
    }

    public void changeAvatar(@NotNull User user, @NotNull String avatar) {
        user.setAvatar(avatar);
        db.changeUserData(user);
    }

    @Nullable
    public User getUser(long id) {
        return db.getUser(id);
    }

    @Nullable
    public User getUser(@NotNull String login) {
        return db.getUser(login);
    }

    public boolean checkLogin(@NotNull String login) {
        return db.getIdByLogin(login) == null;
    }

    public boolean checkEmail(@NotNull String email) {
        return db.getIdByEmail(email) == null;
    }

    public boolean checkSignup(@NotNull String login, @NotNull String email) {
        return db.checkSignup(login, email);
    }

    public boolean checkSignin(long id, @NotNull String password) {
        final User user = db.getUser(id);
        return user != null && encoder.matches(password, user.getPassword());
    }

    public void updateScoreS(long id) {
        db.updateScoreS(id);
    }

    public void updateScoreM(long id) {
        db.updateScoreM(id);
    }

    public List<User> getTopS(Integer limit, Integer since) {
        return db.getTopS(limit, since);
    }

    public List<User> getTopM(Integer limit, Integer since) {
        return db.getTopM(limit, since);
    }

    public void clear() {
        db.clear();
    }

}
