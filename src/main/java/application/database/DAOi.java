package application.database;

import application.models.User;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public interface DAOi {
    @NotNull
    Long addUser(@NotNull String login, @NotNull String password, @NotNull String email);

    void changeUserData(@NotNull User user);

    @Nullable
    User getUser(long userId);

    @Nullable
    User getUser(@NotNull String login);

    @Nullable
    Long getIdByLogin(@NotNull String login);

    @Nullable
    Long getIdByEmail(@NotNull String email);

    boolean checkSignup(@NotNull String login, @NotNull String email);

    @NotNull
    Integer updateScoreS(long userId);

    @NotNull
    Integer updateScoreM(long userId);

    List<User> getTopS(@NotNull Integer limit, @NotNull Integer since);

    List<User> getTopM(@NotNull Integer limit, @NotNull Integer since);

    void clear();
}

