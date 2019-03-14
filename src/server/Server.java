package server;


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

