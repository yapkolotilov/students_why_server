package studentswhyserver.data.personalData;

import org.junit.Test;

import static org.junit.Assert.*;

public class PersonalDataTest {

    @Test
    public void getPassword() {
        PersonalData data = new PersonalData("bla", "bla", "bla");
        data.getPassword();
    }

    @Test
    public void getToken() {
        PersonalData data = new PersonalData("bla", "bla", "bla");
        data.getToken();
    }

    @Test
    public void getLogin() {
        PersonalData data = new PersonalData("bla", "bla", "bla");
        data.getToken();
    }

    @Test
    public void toString1() {
        PersonalData data = new PersonalData("bla", "bla", "bla");
        data.toString();
    }

    @Test
    public void matches() {
        PersonalData data = new PersonalData("bla", "bla", "bla");
        data.matches("bla");
    }

    @Test
    public void matches1() {
        PersonalData data = new PersonalData("bla", "bla", "bla");
        data.matches("a", "b");
    }

    @Test
    public void parseString() {
        try {
            PersonalData data = PersonalData.parseString("bla");
        } catch (Exception e) {

        }
    }
}