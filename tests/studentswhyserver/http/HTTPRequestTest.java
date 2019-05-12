package studentswhyserver.http;

import org.junit.Test;
import studentswhyserver.enums.HTTPMethod;

import static org.junit.Assert.*;

public class HTTPRequestTest {

    @Test
    public void getMethod() {
        HTTPRequest request = new HTTPRequest(HTTPMethod.GET, "");
        request.getMethod();
    }

    @Test
    public void getUrl() {
        HTTPRequest request = new HTTPRequest(HTTPMethod.GET, "");
        request.getUrl();
    }

    @Test
    public void toString1() {
        HTTPRequest request = new HTTPRequest(HTTPMethod.GET, "");
        request.toString();
    }

    @Test
    public void parseString() {
        try {
            HTTPRequest request = HTTPRequest.parseString("bla");
        } catch (Exception e) {
        }
    }

    @Test
    public void getBytes() {
        HTTPRequest request = new HTTPRequest(HTTPMethod.GET, "");
        request.getBytes();
    }

    @Test
    public void getPath() {
        HTTPRequest request = new HTTPRequest(HTTPMethod.GET, "");
        request.getPath();
    }
}