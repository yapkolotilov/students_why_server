package studentswhyserver.enums;

import org.junit.Test;

import static org.junit.Assert.*;

public class HTTPCodeTest {

    @Test
    public void toString1() {
        HTTPCode.OK_200.toString();
    }

    @Test
    public void parseString() {
        HTTPCode.parseString("200 OK");
    }
}