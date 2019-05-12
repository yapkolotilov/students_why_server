package studentswhyserver.data.programs;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void getName() {
        User user = new User("bla", "bla");
        user.getName();
    }

    @Test
    public void getLogin() {
        User user = new User("bla", "bla");
        user.getLogin();
    }

    @Test
    public void toString1() {
        User user = new User("bla", "bla");
        user.toString();
    }
}