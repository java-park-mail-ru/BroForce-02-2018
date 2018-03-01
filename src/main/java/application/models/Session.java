package application.models;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class Session {
    private final Map<String, User> loginToProfileMap = new HashMap<>();
    private final Map<String, User> sessionIDToProfileMap = new HashMap<>();

    public void createUser(User userProfile) {
        loginToProfileMap.put(userProfile.getLogin(), userProfile);
    }

    public void deleteUser(String login) {
        loginToProfileMap.remove(login);
    }

    public void updateUser(String currentLogin, User newUser) {
        deleteUser(currentLogin);
        createUser(newUser);
    }

    public User getUserByLogin(String login) {
        return loginToProfileMap.get(login);
    }

    public User getUserBySessionID(String sessionID) {
        return sessionIDToProfileMap.get(sessionID);
    }

    public void createSession(String sessionID, User user) {
        sessionIDToProfileMap.put(sessionID, user);
    }

    public void deleteSession(String sessionID) {
        sessionIDToProfileMap.remove(sessionID);
    }

    public void updateSession(String sessionID, User user) {
        createSession(sessionID, user);
    }
}
