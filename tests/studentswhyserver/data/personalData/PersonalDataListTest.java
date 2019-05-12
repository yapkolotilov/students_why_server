package studentswhyserver.data.personalData;

import org.junit.Test;
import studentswhyserver.utilities.FilePath;

import static org.junit.Assert.*;

public class PersonalDataListTest {

    PersonalDataList list = PersonalDataList.readFromFile(new FilePath("test.data"));
    @Test
    public void readFromFile() {
        PersonalDataList.readFromFile(new FilePath("test.data"));
    }

    @Test
    public void getByLogin() {
        try {
            list.getByLogin("ad");
        } catch (Exception e) {

        }
    }

    @Test
    public void getByToken() {
        try {
            list.getByToken("ad");
        } catch (Exception e) {

        }
    }

    @Test
    public void containsLogin() {
        list.containsLogin("bla");
    }

    @Test
    public void add() {
        try {
            list.add("bla", "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void remove() {
        try {
            list.remove("ba");
        } catch (Exception e) {

        }
    }

    @Test
    public void validate() {
        try {
            list.validate("bla", "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void validate1() {
        try {
            list.validate("bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void validateSudo() {
        try {
            list.validateSudo("sudo", "sudoPassword");
        } catch (Exception e) {

        }
    }

    @Test
    public void validateSudo1() {
        try {
            list.validateSudo("sudo");
        } catch (Exception e) {

        }
    }

    @Test
    public void generateToken() {
        try {
            list.generateToken("bla", "bla");
        } catch (Exception e) {

        }
    }
}