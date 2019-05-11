package studentswhyserver.data.personalData;

public class PersonalData {
    static final long serialVersionUID = 21L;

    private String login; // Логин.
    private String password; // Пароль.
    private String token; // Токен.

    public PersonalData(String login, String password, String token) {
        this.login = login;
        this.password = password;
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return String.format("%s: %s: %s", login, password, token);
    }

    public boolean matches(String login, String password) {
        return getLogin().equals(login) && getPassword().equals(password);
    }

    public boolean matches(String token) {
        return getToken().equals(token);
    }

    public static PersonalData parseString(String line) {
        String[] lines = line.split(": ");
        return new PersonalData(lines[0], lines[1], lines[2]);
    }
}
