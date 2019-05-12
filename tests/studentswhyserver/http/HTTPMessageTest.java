package studentswhyserver.http;

import org.junit.Test;
import studentswhyserver.enums.HTTPCode;

import static org.junit.Assert.*;

public class HTTPMessageTest {

    @Test
    public void getHeader() {
        HTTPMessage message = new HTTPResponse(HTTPCode.OK_200);
        message.getHeader("da");
    }

    @Test
    public void setHeader() {
        HTTPMessage message = new HTTPResponse(HTTPCode.OK_200);
        message.setHeader("da", "new");
    }

    @Test
    public void containsHeader() {
        HTTPMessage message = new HTTPResponse(HTTPCode.OK_200);
        message.containsHeader("da");
    }

    @Test
    public void getParam() {
        HTTPMessage message = new HTTPResponse(HTTPCode.OK_200);
        message.getParam("dada");
    }

    @Test
    public void setParam() {
        HTTPMessage message = new HTTPResponse(HTTPCode.OK_200);
        message.setParam("d", "1");
    }

    @Test
    public void containsParam() {
        HTTPMessage message = new HTTPResponse(HTTPCode.OK_200);
        message.containsParam("d");
    }

    @Test
    public void getBody() {
        HTTPMessage message = new HTTPResponse(HTTPCode.OK_200);
        message.getBody();
    }

    @Test
    public void setBody() {
        HTTPMessage message = new HTTPResponse(HTTPCode.OK_200);
        message.setBody("dada");
    }

    @Test
    public void setBody1() {
        HTTPMessage message = new HTTPResponse(HTTPCode.OK_200);
        message.setBody(new byte[13]);
    }
}