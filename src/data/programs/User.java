package data.programs;

import java.io.Serializable;

/** Представляет пользователя системы.
 *
 */
public class User implements Serializable {
    static final long serialVersionUID = 34L;

    private String name; // ФИО.
    private String login; // Логин.

    public User(String login, String name) {
        this.name = name; this.login = login;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return String.format("name: %s, login: %s", name, login);
    }
}
