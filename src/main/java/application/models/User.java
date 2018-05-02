package application.models;


public class User {
    private final Long id;
    private String login;
    private String password;
    private String email;
    private String avatar;
    private int scoreS;
    private int scoreM;

    public User(Long id, String login, String password, String email, String ava, int ss, int sm) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.avatar = ava;
        scoreS = ss;
        scoreM = sm;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getScoreS() {
        return scoreS;
    }

    public int getScoreM() {
        return scoreM;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setScoreS(int score) {
        scoreS = score;
    }

    public void setScoreM(int score) {
        scoreM = score;
    }

}