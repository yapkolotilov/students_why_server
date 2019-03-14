package server;


import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import data.items.ItemList;
import data.items.StudentsWhyTree;
import data.personalData.PersonalDataList;
import data.programs.ProgramTree;
import debug.Console;
import utilities.FilePath;
import utilities.GlobalVars;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/** Сервер.
 */
public class Server {
    static ProgramTree programs; // Образовательные программы.
    static PersonalDataList userPasswords; // Пароли пользователей.
    static PersonalDataList adminPasswords; // Пароли администраторов.

    static ItemList newsFeed; // Новостная лента.
    static StudentsWhyTree studentsWhy;

    public static void main(String[] args) {
        ServerSocket serverSocket;

        // Создаём недост. директории:
        File dir = new File("res");
        dir.mkdir();
        dir = new File("res" + File.separator + "public");
        dir.mkdir();
        dir = new File("res" + File.separator + "virtual");
        dir.mkdir();
        dir = new File("res" + File.separator + "sudo");
        dir.mkdir();

        // Загружаем файлы:
        programs = ProgramTree.readFromFile(new FilePath("res", "virtual", "programs.tree"));
        userPasswords = PersonalDataList.readFromFile(new FilePath("res", "sudo", "userPasswords.pas"));
        adminPasswords = PersonalDataList.readFromFile(new FilePath("res", "sudo", "adminPasswords.pas"));
        newsFeed = ItemList.readFromFile(new FilePath("res", "virtual", "newsFeed.list"));
        studentsWhy = StudentsWhyTree.readFromFile(new FilePath("res", "virtual", "studentsWhy.why"));

        // Выводим информацию о данных:
        Console.println("Программы:");
        Console.println(programs + "\n");
        Console.println("Вопросы:");
        Console.println(studentsWhy + "\n");
        Console.println("Новости:");
        Console.println(newsFeed + "\n");


        // Запускаем сервер:
        try {
            serverSocket = new ServerSocket(GlobalVars.serverPort, 0, InetAddress.getByName(args[0]));

            Console.printlnln("Сервер запущен.");
        } catch (IOException e) {
            Console.println("ОШИБКА ПРИ ЗАПУСКЕ СЕРВЕРА!");
            e.printStackTrace();
            return;
        }

        // Слушаем подключения:
        int id = 0;
        while (true) {
            try {
                new Session(serverSocket.accept(), id++);
            } catch (IOException e) {
                Console.println("ОШИБКА ПРИ СОЗДАНИИ СЕССИИ!");
                e.printStackTrace();
            }
        }
    }
}