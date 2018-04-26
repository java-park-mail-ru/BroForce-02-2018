package application.database;

import application.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class DbDAO implements DAOi {
    @Autowired
    private JdbcTemplate template;
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(DbDAO.class);
    private static final Integer SCORE_CHANGE = 25;

    @Override
    @NotNull
    public Long addUser(@NotNull String login, @NotNull String password, @NotNull String email) {
        final String query = "INSERT INTO users(login, password, email) VALUES(?,?,?) RETURNING id";
        return template.queryForObject(query, Long.class, login, password, email);
    }

    @Override
    public void changeUserData(@NotNull User user) {
        try {
            final String query = "UPDATE users SET "
                    + "login = ?, "
                    + "email = ?, "
                    + "password = ? "
                    + "WHERE id = ?";
            template.update(query, user.getLogin(), user.getEmail(), user.getPassword(), user.getId());
        } catch (DuplicateKeyException e) {
            LOGGER.error("DuplicateKeyException in changeUserData");
        }
    }

    private static final RowMapper<User> USER_MAPPER = (res, num) ->
            new User(res.getLong("id"),
                    res.getString("login"),
                    res.getString("password"),
                    res.getString("email")
            );

    @Override
    @Nullable
    public User getUser(long id) {
        try {
            final String query = "SELECT * FROM users WHERE id = ?";
            return template.queryForObject(query, USER_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Nullable
    public User getUser(@NotNull String login) {
        try {
            final String query = "SELECT * FROM users WHERE login = ?";
            return template.queryForObject(query, USER_MAPPER, login);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean checkSignup(@NotNull String login, @NotNull String email) {
        try {
            final String query = "SELECT COUNT(*) FROM users WHERE LOWER(login) = LOWER(?) OR LOWER(email) = LOWER(?)";
            final Long result = template.queryForObject(query, Long.class, login, email);
            return result == 0;
        } catch (EmptyResultDataAccessException e) {
            return true;
        }
    }

    @Override
    @NotNull
    public Integer updateScore(long userId, boolean result) {
        String query = "UPDATE users SET score = score ";
        if (result) {
            query += "+ " + SCORE_CHANGE + ' ';
        } else {
            query += "- " + SCORE_CHANGE + ' ';
        }
        query += "WHERE id = ? RETURNING score";
        return template.queryForObject(query, Integer.class, userId);
    }

    @Override
    @Nullable
    public Long getIdByLogin(@NotNull String login) {
        try {
            final String query = "SELECT id FROM users WHERE LOWER(login) = LOWER(?)";
            return template.queryForObject(query, Long.class, login);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Nullable
    public Long getIdByEmail(@NotNull String email) {
        try {
            final String query = "SELECT id FROM users WHERE LOWER(email) = LOWER(?)";
            return template.queryForObject(query, Long.class, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @NotNull
    public List<User> getTop(@NotNull Integer limit, @NotNull Integer since) {
        final String query = "SELECT * FROM users ORDER BY score DESC LIMIT ? OFFSET ?";
        return template.query(query, USER_MAPPER, limit, since);
    }

    @Override
    public void clear() {
        template.execute("TRUNCATE TABLE users CASCADE");
    }

}
