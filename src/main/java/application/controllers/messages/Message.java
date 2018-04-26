package application.controllers.messages;


public enum Message {
    SIGNED_UP("User signed up"),
    NO_LOGIN_OR_PASSWORD("No login or password"),
    EMPTY_LOGIN_OR_PASSWORD("Empty login or password"),
    WRONG_LOGIN("Wrong login"),
    WRONG_PASSWORD("Wrong password"),
    WRONG_ID("Incorrect session data"),
    EMPTY_DATA("Empty data"),
    LOGIN_IS_ALREADY_TAKEN("Login is already taken"),
    EMAIL_IS_ALREADY_TAKEN("Email is already taken"),
    AUTHORIZED("User authorized"),
    LOGGED_OUT("User logged out"),
    NOT_AUTHORIZED("User not authorized"),
    ALREADY_AUTHORIZED("User is already authorized"),
    USER_PROFILE_UPDATED("User profile data updated");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }
}
