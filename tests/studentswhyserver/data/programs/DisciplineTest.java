package studentswhyserver.data.programs;

import org.junit.Test;
import studentswhyserver.exceptions.DuplicateElemException;

import static org.junit.Assert.*;

public class DisciplineTest {

    @Test
    public void getName() {
        Discipline discipline = new Discipline();
        discipline.getName();
    }

    @Test
    public void setName() {
        Discipline discipline = new Discipline();
        discipline.setName("bla");
    }

    @Test
    public void setType() {
        Discipline discipline = new Discipline();
        discipline.setType("bla");
    }

    @Test
    public void setTutors() {
        Discipline discipline = new Discipline();
        discipline.setTutors(new String[] { "da"});
    }

    @Test
    public void setSchedule() {
        Discipline discipline = new Discipline();
        discipline.setSchedule("bla");
    }

    @Test
    public void toString1() {
        Discipline discipline = new Discipline();
        discipline.toString();
    }
}