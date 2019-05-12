package studentswhyserver.data.programs;

import org.junit.Test;
import studentswhyserver.utilities.FilePath;

import static org.junit.Assert.*;

public class ProgramTreeTest {

    ProgramTree tree = ProgramTree.readFromFile(new FilePath("test.treex"));

    @Test
    public void saveToFile() {
        tree.saveToFile();
    }

    @Test
    public void readFromFile() {
        tree = ProgramTree.readFromFile(new FilePath("test.treex"));
    }

    @Test
    public void getProgram() {
        try {
            tree.getProgram("bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void addNewProgram() {
        try {
            tree.addNewProgram("bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void addNewProgram1() {
        try {
            tree.addNewProgram("bla", "blabla");
        } catch (Exception e) {

        }
    }

    @Test
    public void rebaseProgram() {
        try {
            tree.rebaseProgram("bla", "blabla");
        } catch (Exception e) {

        }
    }

    @Test
    public void removeProgram() {
        try {
            tree.removeProgram("bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void changeProgram() {
        try {
            tree.changeProgram("bla", "newbla");
        } catch (Exception e) {

        }
    }

    @Test
    public void getUser() {
        try {
            tree.getUser("login");
        } catch (Exception e) {

        }
    }

    @Test
    public void addNewUser() {
        try {
            tree.addNewUser("bla", "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void addUserToProgram() {
        try {
            tree.addUserToProgram("bla", "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void removeUserFromProgram() {
        try {
            tree.removeUserFromProgram("login", "password");
        } catch (Exception e) {

        }
    }

    @Test
    public void rebaseUser() {
        try {
            tree.rebaseUser("bla", "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void removeUser() {
        try {
            tree.removeUser("bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void changeUser() {
        try {
            tree.changeUser("bla", "bla", "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void addAdmin() {
        try {
            tree.addAdmin("login");
        } catch (Exception e) {

        }
    }

    @Test
    public void removeAdmin() {
        try {
            tree.removeAdmin("login");
        } catch (Exception e) {

        }
    }

    @Test
    public void getTutor() {
        try {
            tree.getTutor("tutor");
        } catch (Exception e) {

        }
    }

    @Test
    public void addNewTutor() {
        try {
            tree.addNewTutor("bla", new Tutor(), "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void addTutorToProgram() {
        try {
            tree.addTutorToProgram("bla", "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void removeTutorFromProgram() {
        try {
            tree.removeTutorFromProgram("bla", "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void rebaseTutor() {
        try {
            tree.rebaseTutor("bla", "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void removeTutor() {
        try {
            tree.removeTutor("bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void changeTutor() {
        try {
            tree.changeTutor("bla", new Tutor());
        } catch (Exception e) {

        }
    }

    @Test
    public void getDiscipline() {
        try {
            tree.getDiscipline("bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void addNewDiscipline() {
        try {
            tree.addNewDiscipline("bla", new Discipline(), "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void addDisciplineToProgram() {
        try {
            tree.addDisciplineToProgram("bla", "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void removeDisciplineFromProgram() {
        try {
            tree.removeDisciplineFromProgram("bla", "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void rebaseDiscipline() {
        try {
            tree.rebaseDiscipline("bla", "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void removeDiscipline() {
        try {
            tree.removeDiscipline("bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void changeDiscipline() {
        try {
            tree.changeDiscipline("bla", new Discipline());
        } catch (Exception e) {

        }
    }

    @Test
    public void getUsersJson() {
        try {
            tree.getUsersJson();
        } catch (Exception e) {

        }
    }

    @Test
    public void getTutorsJson() {
        try {
            tree.getTutorsJson();
        } catch (Exception e) {

        }
    }

    @Test
    public void getProgramsJson() {
        try {
            tree.getProgramsJson();
        } catch (Exception e) {

        }
    }

    @Test
    public void getDisciplinesJson() {
        try {
            tree.getDisciplinesJson();
        } catch (Exception e) {

        }
    }

    @Test
    public void getUsersProgramsJSON() {
        try {
            tree.getUsersProgramsJSON("bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void getUsersTutorsJSON() {
        try {
            tree.getUsersTutorsJSON("bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void getUsersDisciplinesJSON() {
        try {
            tree.getUsersDisciplinesJSON("bla");
        } catch (Exception e) {

        }
    }
}