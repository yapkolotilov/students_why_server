package studentswhyserver.http;

import org.junit.Test;
import studentswhyserver.enums.HTTPCode;
import studentswhyserver.enums.Result;

import static org.junit.Assert.*;

public class HTTPResponseTest {

    @Test
    public void setResult() {
        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        response.setResult(Result.SUCCESS);
    }

    @Test
    public void toString1() {
        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        response.toString();
    }

    @Test
    public void getBytes() {
        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        response.getBytes();
    }

    @Test
    public void parseString() {
        try {
            HTTPResponse response = HTTPResponse.parseString("line");
        } catch (Exception e) {

        }
    }
}