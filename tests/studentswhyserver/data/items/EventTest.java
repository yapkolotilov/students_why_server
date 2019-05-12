package studentswhyserver.data.items;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventTest {

    @Test
    public void setDate() {
        Event event = new Event();
        event.setDate("01.01.1980");
    }

    @Test
    public void toString1() {
        Assert.assertEquals(new Event().toString(), "header: null, content: nulldate: null, coords: 0.0x0.0, place: null");
    }
}