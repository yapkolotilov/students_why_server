package studentswhyserver.enums;

import org.junit.Test;

import static org.junit.Assert.*;

public class HTTPMethodTest {

    @Test
    public void toString1() {
        HTTPMethod.GET.toString();
    }

    @Test
    public void parseString() {
        try {
            HTTPMethod.parseString("GET");
        } catch (Exception e) {

        }
    }
}