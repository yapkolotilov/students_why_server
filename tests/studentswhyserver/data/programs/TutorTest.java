package studentswhyserver.data.programs;

import org.junit.Test;

import static org.junit.Assert.*;

public class TutorTest {

    @Test
    public void getName() {
        Tutor tutor = new Tutor();
        tutor.getName();
    }

    @Test
    public void setName() {
        Tutor tutor = new Tutor();
        tutor.setName("bla");
    }

    @Test
    public void setPhones() {
        Tutor tutor = new Tutor();
        tutor.setPhones("bla", "bla");
    }

    @Test
    public void setEmails() {
        Tutor tutor = new Tutor();
        tutor.setEmails("bla", "bla");
    }

    @Test
    public void setDisciplines() {
        Tutor tutor = new Tutor();
        tutor.setDisciplines("bla", "bla");
    }

    @Test
    public void setPosts() {
        Tutor tutor = new Tutor();
        tutor.setPosts("bla", "bla");
    }

    @Test
    public void setUrl() {
        Tutor tutor = new Tutor();
        tutor.setUrl("bla");
    }

    @Test
    public void setImgURL() {
        Tutor tutor = new Tutor();
        tutor.setImgURL("bla");
    }

    @Test
    public void toString1() {
        Tutor tutor = new Tutor();
        tutor.toString();
    }
}