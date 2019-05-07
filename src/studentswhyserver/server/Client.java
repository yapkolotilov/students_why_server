package studentswhyserver.server;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.SAXConnector;
import studentswhyserver.debug.Console;
import studentswhyserver.enums.HTTPMethod;
import studentswhyserver.http.HTTPRequest;
import studentswhyserver.http.HTTPResponse;
import studentswhyserver.utilities.GlobalMethods;
import studentswhyserver.utilities.GlobalVars;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Пример клиента-администратора.
 */
public class Client {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Введите ваш запрос или quit:");
                String line = scanner.nextLine();

                try {
                    // Проверяем условие выхода:
                    if (line.toLowerCase().equals("quit") || line.toLowerCase().equals("q"))
                        break;

                    // Получаем метод URL:
                    String[] lineSplit = line.split(" ", 3);
                    HTTPRequest request = new HTTPRequest(HTTPMethod.parseString(lineSplit[0]), lineSplit[1]);
                    line = lineSplit[2]; line = " " + line;

                    // Средства парсинга:
                    Pattern pattern = Pattern.compile(" -\\S+");
                    Matcher matcher = pattern.matcher(line);
                    String[] values = line.split(" -\\S+ ");

                    // Конструируем запрос:
//                    System.out.println(Arrays.toString(values));
                    int i = 1;
                    while (matcher.find()) {
                        String name = matcher.group().substring(2); // Название заголовка.
//                        System.out.println(name);
                        String value = values[i++];

                        if (name.equals("Body"))
                            request.setBody(value);
                        else
                            request.setHeader(name, value);
                    }
                    Socket socket = new Socket(args[0], 80);
                    socket.getOutputStream().write(request.getBytes());

                    // Считываем ответ:
                    String responseStr = "";
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while ((line = reader.readLine()) != null)
                        responseStr += line + "\n";
                    HTTPResponse response = HTTPResponse.parseString(responseStr);
                    System.out.println(response);
                    System.out.println();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Неправильный запрос! Повторите попытку:");
                }
            }
        } catch (Exception e) {
            Console.println("Ошибка при подключении!");
            e.printStackTrace();
        }
    }
}
