package server;

import com.google.gson.Gson;
import data.programs.Discipline;
import data.programs.Tutor;
import debug.Console;
import enums.HTTPMethod;
import http.HTTPRequest;
import utilities.GlobalMethods;
import utilities.GlobalVars;

import java.io.IOException;
import java.net.Socket;

/** Пример клиента-администратора.
 */
public class Client {
    public static void main(String[] args) {
        try {
            // -2059407004 - для yapkolotilov
            // -451735726 - суперадмин
            Socket socket = new Socket(args[0], GlobalVars.serverPort);

            HTTPRequest request = new HTTPRequest(HTTPMethod.PUT, "/");
            // Сюда добавить заголовки.


            Console.println("Сформирован запрос:");
            Console.printlnln(request);
            socket.getOutputStream().write(request.getBytes());
            socket.getOutputStream().flush();

            String response = GlobalMethods.readStrResponse(socket.getInputStream());
            Console.println("Получен ответ:");
            Console.println(response);
        } catch (Exception e) {
            Console.println("ОШИБКА В КЛИЕНТЕ!");
            e.printStackTrace();
        }
    }
}
