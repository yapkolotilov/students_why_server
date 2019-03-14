package server;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.awt.SecurityWarning;
import data.items.Event;
import data.items.Item;
import data.personalData.PersonalData;
import data.personalData.PersonalDataList;
import data.programs.Discipline;
import data.programs.Tutor;
import data.programs.User;
import data.tree.DataTreeLeaf;
import data.tree.DataTreeNode;
import debug.Console;
import enums.HTTPCode;
import enums.Result;
import exceptions.*;
import http.HTTPRequest;
import http.HTTPResponse;
import org.omg.CORBA.OBJ_ADAPTER;
import utilities.FilePath;
import utilities.GlobalMethods;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

/** Сессия, отвечающая за обработку отдельных запросов клиентов.
 */
public class Session extends Thread {
    private Socket clientSocket; // Сокет клиента.
    private OutputStream outputStream; // Выходной поток данных.
    private InputStream inputStream; // Входной поток данных.
    private int id; // Номер сессии.
    private HTTPRequest request; // Запрос пользователя.

    /** Запускает новую сессию работы с клиентом.
     *
     * @param clientSocket Клиентский сокет.
     * @param id Номер сессии.
     */
    Session(Socket clientSocket, int id) throws IOException {
        this.clientSocket = clientSocket;
        inputStream = clientSocket.getInputStream();
        outputStream = clientSocket.getOutputStream();
        this.id = id;

        printlnlnMessage("Начата.");
        start();
    }

    @Override
    public void run() {
        try {
            // Получаем сообщение:
            String strRequest = GlobalMethods.readStrRequest(inputStream);

            request = null;
            try {
                // Формируем запрос:
                printlnMessage("Получен запрос:");
                Console.printlnln(strRequest);
                request = HTTPRequest.parseString(strRequest);

                // Отвечаем клиенту:
                HTTPResponse response; // Ответ клиенту.
                switch (request.getMethod()) {
                    case GET:
                        response = proceedGET();
                        break;
                    case HEAD:
                        response = proceedHEAD();
                        break;
                    case POST:
                        response = proceedPOST();
                        break;
                    case PUT:
                        response = proceedPUT();
                        break;
                    case REMOVE:
                        response = proceedREMOVE();
                    default:
                        throw new NotImplementedException();
                }
                sendResponse(response);
            } catch (StringFormatException e) {
                HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
                response.setBody("Запрос сформирован неверно!");
                sendResponse(response);
            } catch (MethodNotSupportedException e) {
                HTTPResponse response = new HTTPResponse(HTTPCode.METHOD_NOT_ALLOWED_405);
                response.setBody("Данный метод не поддерживается.");
                sendResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
                HTTPResponse response = new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
                sendResponse(response);
            }
        } catch (IOException e) {
            Console.println("ОШИБКА ПРИ ОБРАБОТКЕ СЕССИИ!");
            e.printStackTrace();
        }

        // Выводим информацию о данных:
        Console.println("Программы:");
        Console.println(Server.programs + "\n");
        Console.println("Вопросы:");
        Console.println(Server.studentsWhy + "\n");
        Console.println("Новости:");
        Console.println(Server.newsFeed + "\n");
    }


    // --- Методы обработки запросов ---

    private HTTPResponse proceedGET() {
        FilePath path = request.getPath();
        Console.println("ПУТЬ: " + path.get(1));

        if (path.size() < 3 || path.contains("..") || !path.get(0).equals("res")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("url запроса файла должна начинаться с res!");
            return response;
        }

        if (path.get(1).equals("virtual")) {
            switch (path.get(2)) {
                case "programs":
                    if (path.size() >= 4)
                        switch (path.get(3)) {
                            case "users":
                                if (path.size() >= 5) {
                                    if (path.size() >= 6)
                                        switch (path.get(5)) {
                                            case "programs":
                                                return proceedGetUsersPrograms();
                                            case "tutors":
                                                return proceedGetUsersTutors();
                                            case "disciplines":
                                                return proceedGetUsersDisciplines();
                                        }
                                    return proceedGetUser();
                                }
                                return proceedGetUsers();

                            case "programs":
                                if (path.size() >= 5)
                                    return proceedGetProgram();
                                return proceedGetPrograms();

                            case "tutors":
                                if (path.size() >= 5)
                                    return proceedGetTutor();
                                return proceedGetTutors();

                            case "disciplines":
                                if (path.size() >= 5)
                                    return proceedGetDiscipline();
                                return proceedGetDisciplines();
                        }
                    return proceedGetPrograms();

                case "news":
                    if (request.containsHeader("Number"))
                        return proceedGetLastNews();
                    if (path.size() >= 4)
                        return proceedGetItem();
                    return proceedGetNews();


                case "events":
                    if (request.containsHeader("Number"))
                        return proceedGetLastEvents();
                    if (path.size() >= 4)
                        return proceedGetEvent();
                    return proceedGetEvents();

                case "studentsWhy":
                    if (path.size() >= 4)
                        switch (path.get(3)) {
                            case "tags":
                                if (path.size() >= 5)
                                    return proceedGetTag();
                                return proceedGetTags();
                            case "questions":
                                if (path.size() >= 5) {
                                    if (path.size() >= 6)
                                        return proceedGetQuestionsTags();
                                    return proceedGetQuestion();
                                }
                                return proceedGetQuestions();
                        }
            }
        }

        if (path.get(1).equals("public")) {
            File file = new File(path.toString());
            if (!file.exists()) {
                HTTPResponse response = new HTTPResponse(HTTPCode.NOT_FOUND_404);
                response.setBody("Данный файл не существует!");
                return response;
            }

            try {
                HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
                response.setResult(Result.SUCCESS);
//                response.setHeader("File-Format", path.toString().split(".")[path.toString().split(".").length - 1]);
                response.setBody(Files.readAllBytes(file.toPath()));
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                HTTPResponse response = new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
                response.setBody("Ошибка при чтении файла!");
                return response;
            }
        }

        HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
        response.setBody("Неправильно выбран каталог! Выберите public или virtual!");
        return response;
    }

    private HTTPResponse proceedHEAD() {
        return new HTTPResponse(HTTPCode.BAD_REQUEST_400);
    }

    private HTTPResponse proceedPOST() {
        if (request.containsHeader("Action")) {
            String action = request.getHeader("Action");
            switch (action) {
                case "Register":
                    return proceedRegister();
                case "Log-In":
                    return proceedLogIn();
                case "Change-Personal-Data":
                    return proceedChangePersonalData();
            }
        }

        FilePath path = request.getPath();
        if (path.size() < 3 || path.contains("..") || !path.get(0).equals("res")) {
            return new HTTPResponse(HTTPCode.BAD_REQUEST_400);
        }

        if (path.get(1).equals("public")) {
            if (!checkHeaders("Token", "File-Format")) {
                HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
                response.setBody("Нужны заголовки Token, File-Format!");
                return response;
            }

            String token = request.getHeader("Token");
            String fileFormat = request.getHeader("File-Format");
            String content = new String(request.getBody());

            if (!Server.adminPasswords.validate(token)) {
                HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
                response.setResult(Result.NO_RIGHTS);
                response.setBody("Публиковать файлы могут только администраторы!");
                return response;
            }

            String fileID = getFileID(content);
            FilePath filePath = new FilePath(path);
            filePath.add(String.format("%s.%s", fileID, fileFormat));

            File file = new File(filePath.toString());
            if (file.exists()) {
                HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
                response.setResult(Result.ALREADY_DONE);
                response.setBody(fileID);
                return response;
            }
            try {
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(content.getBytes());
                outputStream.close();

                HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
                response.setResult(Result.SUCCESS);
                response.setBody(fileID);
                return response;
            } catch (IOException e) {
                HTTPResponse response = new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
                response.setBody("Ошибка при загрузке файла!");
                return response;
            }
        }

        // Если нам нечего обрабатывать.
        HTTPResponse result = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
        result.setBody("Неправильный запрос!");
        return result;
    }

    private HTTPResponse proceedPUT() {
        if (request.containsHeader("Action")) {
            String action = request.getHeader("Action");
            switch (action) {
                case "Remove-User":
                    return proceedRemoveUser();
                case "Rebase-User":
                    return proceedRebaseUser();
                case "Add-User-To-Program":
                    return proceedAddUserToProgram();
                case "Remove-User-From-Program":
                    return proceedRemoveUserFromProgram();

                case "Add-Admin":
                    return proceedAddAdmin();
                case "Remove-Admin":
                    return proceedRemoveAdmin();


                case "Add-Program":
                    return proceedAddProgram();
                case "Rebase-Program":
                    return proceedRebaseProgram();
                case "Remove-Program":
                    return proceedRemoveProgram();
                case "Change-Program":
                    return proceedChangeProgram();

                case "Add-Tutor":
                    return proceedAddTutor();
                case "Change-Tutor":
                    return proceedChangeTutor();
                case "Rebase-Tutor":
                    return proceedRebaseTutor();
                case "Add-Tutor-To-Program":
                    return proceedAddTutorToProgram();
                case "Remove-Tutor-From-Program":
                    return proceedRemoveTutorFromProgram();
                case "Remove-Tutor":
                    return proceedRemoveTutor();

                case "Add-Discipline":
                    return proceedAddDiscipline();
                case "Change-Discipline":
                    return proceedChangeDiscipline();
                case "Rebase-Discipline":
                    return proceedRebaseDiscipline();
                case "Add-Discipline-To-Program":
                    return proceedAddDisciplineToProgram();
                case "Remove-Discipline-From-Program":
                    return proceedRemoveDisciplineFromProgram();
                case "Remove-Discipline":
                    return proceedRemoveDiscipline();

                case "Add-Item":
                    return proceedAddItem();
                case "Add-Event":
                    return proceedAddEvent();
                case "Change-Item":
                    return proceedChangeItem();
                case "Change-Event":
                    return proceedChangeEvent();
                case "Remove-Item":
                    return proceedRemoveItem();

                case "Add-Tag":
                    return proceedAddTag();
                case "Remove-Tag":
                    return proceedRemoveTag();
                case "Change-Tag":
                    return proceedChangeTag();
                case "Rebase-Tag":
                    return proceedRebaseTag();

                case "Add-Question":
                    return proceedAddQuestion();
                case "Change-Question":
                    return proceedChangeQuestion();
                case "Rebase-Question":
                    return proceedRebaseQuestion();
                case "Add-Question-To-Tag":
                    return proceedAddQuestionToTag();
                case "Remove-Question-From-Tag":
                    return proceedRemoveQuestionFromTag();
                case "Remove-Question":
                    return proceedRemoveQuestion();
            }
        }

        // Если нам нечего обрабатывать.
        HTTPResponse result = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
        result.setBody("Отсутствует заголовок Action!");
        return result;
    }

    private HTTPResponse proceedREMOVE() {
        FilePath path = request.getPath();
        if (path.get(1).equals("public")) {
            if (!checkHeaders("Token")) {
                HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
                response.setBody("Нужны заголовки Token!");
                return response;
            }

            String token = request.getHeader("Token");
            String content = new String(request.getBody());

            if (!Server.adminPasswords.validate(token)) {
                HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
                response.setResult(Result.NO_RIGHTS);
                response.setBody("Удалять файлы могут только администраторы!");
                return response;
            }

            String fileID = getFileID(content);
            FilePath filePath = new FilePath(path);

            File file = new File(filePath.toString());
            if (!file.exists()) {
                HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
                response.setResult(Result.ALREADY_DONE);
                return response;
            }
            file.delete();

            HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
            response.setResult(Result.SUCCESS);
            response.setBody(fileID);
            return response;
        }

        HTTPResponse result = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
        result.setBody("Неправильный запрос!");
        return result;

    }


    // --- POST ---

    private HTTPResponse proceedRegister() {
        if (!checkHeaders("Login", "Password", "Name")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Login, Password, Name!");
            return response;
        }

        String login = request.getHeader("Login");
        String password = request.getHeader("Password");
        String name = request.getHeader("Name");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);

        if (PersonalDataList.validateSudo(login, password)) {
            response.setResult(Result.ALREADY_DONE);
            response.setBody("Нельзя зарегистировать нового суперадмина!");

            return response;
        }

        try {
            String token = Server.userPasswords.add(login, password);
            Server.programs.addNewUser(login, name);

            response.setResult(Result.SUCCESS);
            response.setParam("token", token);
            return response;
        } catch (DuplicateElemException e) {
            response.setResult(Result.ALREADY_DONE);
            response.setBody("Такой пользователь уже зарегистрирован!");
            return response;
        }
    }

    private HTTPResponse proceedLogIn() {
        if (!checkHeaders("Login", "Password")) {
            if (!checkHeaders("Token")) {
                HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
                response.setBody("Нужны заголовки Login, Password или Token!");
                return response;
            } else {
                String token = request.getHeader("Token");
                HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
                // Логин как обычный юзер:
                if (Server.userPasswords.validate(token)) {
                    try {
                        PersonalData data = Server.userPasswords.getByToken(token);
                        response.setResult(Result.SUCCESS);
                        response.setHeader("User-Type", "User");

                        response.setParam("login", data.getLogin());
                        response.setParam("name", Server.programs.getUser(data.getLogin()).getName());
                        response.setParam("token", data.getToken());

                        return response;
                    } catch (NoSuchElemException e) {
                        e.printStackTrace();
                        return new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
                    }
                }

                // Логин как администратор:
                if (Server.adminPasswords.validate(token)) {
                    try {
                        PersonalData data = Server.adminPasswords.getByToken(token);
                        response.setResult(Result.SUCCESS);
                        response.setHeader("User-Type", "User");

                        response.setParam("login", data.getLogin());
                        response.setParam("name", Server.programs.getUser(data.getLogin()).getName());
                        response.setParam("token", data.getToken());

                        return response;
                    } catch (NoSuchElemException e) {
                        e.printStackTrace();
                        return new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
                    }
                }

                response.setResult(Result.FAIL);
                response.setBody("Неправильные данные!");
                return response;
            }

        }

        String login = request.getHeader("Login");
        String password = request.getHeader("Password");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);

        // Логин как суперадмин:
        if (Server.adminPasswords.validateSudo(login, password)) {
            response.setResult(Result.SUCCESS);
            response.setHeader("User-Type", "User");

            response.setParam("login", PersonalDataList.sudoData.getLogin());
            response.setParam("name", "SuperAdmin");
            response.setParam("token", PersonalDataList.sudoData.getToken());

            return response;
        }

        // Логин как обычный юзер:
        if (Server.userPasswords.validate(login, password)) {
            try {
                PersonalData data = Server.userPasswords.getByLogin(login);
                response.setResult(Result.SUCCESS);
                response.setHeader("User-Type", "User");

                response.setParam("login", data.getLogin());
                response.setParam("name", Server.programs.getUser(login).getName());
                response.setParam("token", data.getToken());

                return response;
            } catch (NoSuchElemException e) {
                e.printStackTrace();
                return new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
            }
        }

        // Логин как администратор:
        if (Server.adminPasswords.validate(login, password)) {
            try {
                PersonalData data = Server.adminPasswords.getByLogin(login);
                response.setResult(Result.SUCCESS);
                response.setHeader("User-Type", "User");

                response.setParam("login", data.getLogin());
                response.setParam("name", Server.programs.getUser(login).getName());
                response.setParam("token", data.getToken());

                return response;
            } catch (NoSuchElemException e) {
                e.printStackTrace();
                return new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
            }
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedChangePersonalData() {
        if (!checkHeaders("Token", "New-Login", "New-Password", "New-Name")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, New-Login, New-Password и New-Name!");
            return response;
        }

        String token = request.getHeader("Token");
        String newLogin = request.getHeader("New-Login");
        String newPassword = request.getHeader("New-Password");
        String newName = request.getHeader("New-Name");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.userPasswords.validate(token)) {
            try {
                PersonalData data = Server.userPasswords.getByToken(token);
                Server.userPasswords.remove(token);
                String newToken = Server.userPasswords.add(newLogin, newPassword);
                Server.programs.changeUser(data.getLogin(), newLogin, newName);

                response.setResult(Result.SUCCESS);
                response.setHeader("User-Type", "User");
                response.setParam("token", newToken);
                return response;
            } catch (NoSuchElemException e) {
                e.printStackTrace();
                return new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
            } catch (DuplicateElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Пользователь с таким логином уже зарегистрирован!");
                return response;
            }
        }

        if (Server.adminPasswords.validate(token)) {
            try {
                PersonalData data = Server.adminPasswords.getByToken(token);
                Server.adminPasswords.remove(token);
                String newToken = Server.adminPasswords.add(newLogin, newPassword);
                Server.programs.changeUser(data.getLogin(), newLogin, newName);

                response.setResult(Result.SUCCESS);
                response.setHeader("User-Type", "Admin");
                response.setParam("token", newToken);
                return response;
            } catch (NoSuchElemException e) {
                e.printStackTrace();
                return new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
            } catch (DuplicateElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Пользователь с таким логином уже зарегистрирован!");
                return response;
            }
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }


    /// --- PUT ---

    private HTTPResponse proceedRemoveUser() {
        if (!checkHeaders("Token")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Не хватает заголовка Token!");
            return response;
        }

        String token = request.getHeader("Token");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.userPasswords.validateSudo(token)) {
            try {
                if (!checkHeaders("Login")) {
                    response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
                    response.setBody("Нужен заголовок Login!");
                    return response;
                }
                String login = request.getHeader("Login");
                Server.programs.removeUser(login);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                e.printStackTrace();
                return new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
            }
        }

        if (Server.userPasswords.validate(token)) {
            try {
                String login = Server.userPasswords.getByToken(token).getLogin();
                Server.userPasswords.remove(token);
                Server.programs.removeUser(login);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                e.printStackTrace();
                return new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
            }
        }

        if (Server.adminPasswords.validate(token)) {
            try {
                String login = Server.adminPasswords.getByToken(token).getLogin();
                Server.adminPasswords.remove(token);
                Server.programs.removeUser(login);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                e.printStackTrace();
                return new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
            }
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedAddUserToProgram() {
        if (!checkHeaders("Token", "Login", "Program")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Не хватает заголовков Token, Login, Program!");
            return response;
        }

        String token = request.getHeader("Token");
        String login = request.getHeader("Login");
        String program = request.getHeader("Program");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.addUserToProgram(login, program);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Не существует пользователя с таким именем!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Данный пользователь уже принадлежит этой образовательной программе!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для совершения этого действия!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedRemoveUserFromProgram() {
        if (!checkHeaders("Token", "Login", "Program")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Не хватает заголовков Token, Login, Program!");
            return response;
        }

        String token = request.getHeader("Token");
        String login = request.getHeader("Login");
        String program = request.getHeader("Program");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.removeUserFromProgram(login, program);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Не существует пользователя с таким именем!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Данный пользователь уже принадлежит этой образовательной программе!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для совершения этого действия!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedRebaseUser() {
        if (!checkHeaders("Token", "Login", "Program")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Не хватает заголовков Token, Login, Program!");
            return response;
        }

        String program = request.getHeader("Program");
        String token = request.getHeader("Token");
        String login = request.getHeader("Login");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.rebaseUser(login, program);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Пользователя с данным логином нет в базе данных!");
                return response;
            } catch (DuplicateElemException e) {
                e.printStackTrace();
                response.setResult(Result.ALREADY_DONE);
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для перемещения пользователей!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }


    private HTTPResponse proceedAddAdmin() {
        if (!checkHeaders("Token", "Login")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Необходимы заголовки Token и Login!");
            return response;
        }

        String token = request.getHeader("Token");
        String login = request.getHeader("Login");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.addAdmin(login);
                PersonalData data = Server.userPasswords.getByLogin(login);
                Server.userPasswords.remove(data.getToken());
                Server.adminPasswords.add(data.getLogin(), data.getPassword());

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("В системе нет пользователя с таким логином!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Данный пользователь уже является администратором!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для совершения данного действия!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedRemoveAdmin() {
        if (!checkHeaders("Token", "Login")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Необходимы заголовки Token и Login!");
            return response;
        }

        String token = request.getHeader("Token");
        String login = request.getHeader("Login");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.removeAdmin(login);
                PersonalData data = Server.adminPasswords.getByLogin(login);
                Server.adminPasswords.remove(data.getToken());
                Server.userPasswords.add(data.getLogin(), data.getPassword());

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                e.printStackTrace();
                response.setResult(Result.FAIL);
                response.setBody("В системе нет пользователя с таким логином!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("С данного пользователя уже сняты полномочия администратора!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для совершения данного действия!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }


    private HTTPResponse proceedAddProgram() {
        if (!checkHeaders("Token", "Program")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Action, Token и Program!");
            return response;
        }

        String token = request.getHeader("Token");
        String program = request.getHeader("Program");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                if (request.containsHeader("Base-Program"))
                    Server.programs.addNewProgram(program, request.getHeader("Base-Program"));
                else
                    Server.programs.addNewProgram(program);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Нет программы с таким названием!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Такая программа уже существует!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас не хватает прав для создания программы!");
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedRebaseProgram() {
        if (!checkHeaders("Token", "Program", "Base-Program")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, Program, Base-Program!");
            return response;
        }

        String token = request.getHeader("Token");
        String program = request.getHeader("Program");
        String baseProgram = request.getHeader("Base-Program");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.rebaseProgram(program, baseProgram);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Нет образовательной программы с таким названием!");
                return response;
            } catch (DuplicateElemException e) {
                e.printStackTrace();
                return new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для перемещения программы!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedRemoveProgram() {
        if (!checkHeaders("Token", "Program")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Action, Token и Program!");
            return response;
        }

        String token = request.getHeader("Token");
        String program = request.getHeader("Program");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.removeProgram(program);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Нет программы с таким названием!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Такая программа уже существует!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас не хватает прав для удаления программы!");
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedChangeProgram() {
        if (!checkHeaders("Token", "Program", "New-Program")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Action, Token и Program!");
            return response;
        }

        String token = request.getHeader("Token");
        String program = request.getHeader("Program");
        String newProgram = request.getHeader("New-Program");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.changeProgram(program, newProgram);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Нет программы с таким названием!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Такая программа уже существует!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас не хватает прав для изменения программы!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }


    private HTTPResponse proceedAddTutor() {
        if (!checkHeaders("Token", "Program") || request.getBody() == null || request.getBody().length == 0) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, Program и JSON-преподаватель в теле!");
            return response;
        }

        String token = request.getHeader("Token");
        String program = request.getHeader("Program");


        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Gson gson = new Gson();
                String body = new String(request.getBody());
                Tutor tutor = gson.fromJson(body, Tutor.class);

                Console.println(body.length());
                Server.programs.addNewTutor(tutor.getName(), tutor, program);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                e.printStackTrace();
                Console.println("Ошибка при добавлении преподавателя!");
                return new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
            } catch (DuplicateElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Преподаватель с таким именем уже существует!");
                return response;
            } catch (JsonSyntaxException e) {
                response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
                response.setBody("Неправильный формат преподавателя!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для добавления преподавателя!");
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedChangeTutor() {
        if (!checkHeaders("Token", "Tutor") || request.getBody() == null || request.getBody().length == 0) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, Tutor и преподаватель в теле!");
            return response;
        }

        String token = request.getHeader("Token");
        String tutorName = request.getHeader("Tutor");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Gson gson = new Gson();
                String body = new String(request.getBody());
                Tutor tutor = gson.fromJson(body, Tutor.class);
                Server.programs.changeTutor(tutorName, tutor);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Нет такого преподавателя!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Данный преподаватель уже существует!");
                return response;
            } catch (JsonSyntaxException e) {
                response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
                response.setBody("Неправильный формат преподавателя!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для изменения преподавателя!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedRebaseTutor() {
        if (!checkHeaders("Token", "Tutor", "Program")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, Tutor, Program");
            return response;
        }

        String token = request.getHeader("Token");
        String tutor = request.getHeader("Tutor");
        String program = request.getHeader("Program");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.rebaseTutor(tutor, program);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Нет такого преподавателя!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Данный преподаватель уже принадлежит данной образовательной программе!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для перемещения преподавателя!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedAddTutorToProgram() {
        if (!checkHeaders("Token", "Tutor", "Program")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, Tutor, Program");
            return response;
        }

        String token = request.getHeader("Token");
        String tutor = request.getHeader("Tutor");
        String program = request.getHeader("Program");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.addTutorToProgram(tutor, program);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Нет такого преподавателя!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Данный преподаватель уже принадлежит данной образовательной программе!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для добавления преподавателя!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedRemoveTutorFromProgram() {
        if (!checkHeaders("Token", "Tutor", "Program")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, Tutor, Program");
            return response;
        }

        String token = request.getHeader("Token");
        String tutor = request.getHeader("Tutor");
        String program = request.getHeader("Program");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.removeTutorFromProgram(tutor, program);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Нет такого преподавателя!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Данный преподаватель уже принадлежит данной образовательной программе!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для добавления преподавателя!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedRemoveTutor() {
        if (!checkHeaders("Token", "Tutor")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, Tutor, Program");
            return response;
        }

        String token = request.getHeader("Token");
        String tutor = request.getHeader("Tutor");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.removeTutor(tutor);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Нет такого преподавателя!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для добавления преподавателя!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }


    private HTTPResponse proceedAddDiscipline() {
        if (!checkHeaders("Token", "Program") || request.getBody() == null || request.getBody().length == 0) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, Program и JSON-дисциплина в теле!");
            return response;
        }

        String token = request.getHeader("Token");
        String program = request.getHeader("Program");


        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Gson gson = new Gson();
                String body = new String(request.getBody());
                Discipline discipline = gson.fromJson(body, Discipline.class);
                Server.programs.addNewDiscipline(discipline.getName(), discipline, program);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                e.printStackTrace();
                return new HTTPResponse(HTTPCode.INTERNAL_SERVER_ERROR_500);
            } catch (DuplicateElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Дисциплина с таким именем уже существует!");
                return response;
            } catch (JsonSyntaxException e) {
                response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
                response.setBody("Неправильный формат дисциплины!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для добавления преподавателя!");
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedChangeDiscipline() {
        if (!checkHeaders("Token", "Discipline") || request.getBody() == null || request.getBody().length == 0) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, Discipline и дисциплина в теле!");
            return response;
        }

        String token = request.getHeader("Token");
        String disciplineName = request.getHeader("Discipline");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Gson gson = new Gson();
                String body = new String(request.getBody());
                Discipline discipline = gson.fromJson(body, Discipline.class);
                Server.programs.changeDiscipline(disciplineName, discipline);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Нет такого преподавателя!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Данный преподаватель уже существует!");
                return response;
            } catch (JsonSyntaxException e) {
                response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
                response.setBody("Неправильный формат преподавателя!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для изменения преподавателя!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedRebaseDiscipline() {
        if (!checkHeaders("Token", "Discipline", "Program")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, Discipline, Program");
            return response;
        }

        String token = request.getHeader("Token");
        String discipline = request.getHeader("Discipline");
        String program = request.getHeader("Program");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.rebaseDiscipline(discipline, program);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Нет такой дисциплины!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Данная дисциплина уже принадлежит данной образовательной программе!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для перемещения дисциплины!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedAddDisciplineToProgram() {
        if (!checkHeaders("Token", "Discipline", "Program")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, Discipline, Program");
            return response;
        }

        String token = request.getHeader("Token");
        String discipline = request.getHeader("Discipline");
        String program = request.getHeader("Program");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.addDisciplineToProgram(discipline, program);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Нет такой дисциплины!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Данная дисциплина уже принадлежит данной образовательной программе!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для добавления дисциплины!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedRemoveDisciplineFromProgram() {
        if (!checkHeaders("Token", "Discipline", "Program")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, Discipline, Program");
            return response;
        }

        String token = request.getHeader("Token");
        String discipline = request.getHeader("Discipline");
        String program = request.getHeader("Program");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.removeDisciplineFromProgram(discipline, program);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Нет такой дисциплины!");
                return response;
            } catch (DuplicateElemException e) {
                response.setResult(Result.FAIL);
                response.setBody("Данная дисциплина уже принадлежит данной образовательной программе!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для удаления дисциплины!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }

    private HTTPResponse proceedRemoveDiscipline() {
        if (!checkHeaders("Token", "Discipline")) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужны заголовки Token, Discipline, Program");
            return response;
        }

        String token = request.getHeader("Token");
        String discipline = request.getHeader("Discipline");

        HTTPResponse response = new HTTPResponse(HTTPCode.OK_200);
        if (Server.adminPasswords.validate(token)) {
            try {
                Server.programs.removeDiscipline(discipline);

                response.setResult(Result.SUCCESS);
                return response;
            } catch (NoSuchElemException e) {
                response.setResult(Result.ALREADY_DONE);
                response.setBody("Нет такой дисциплины!");
                return response;
            }
        }

        if (Server.userPasswords.validate(token)) {
            response.setResult(Result.NO_RIGHTS);
            response.setBody("У вас недостаточно прав для удаления дисциплины!");
            return response;
        }

        response.setResult(Result.FAIL);
        response.setBody("Неправильные данные!");
        return response;
    }


    private HTTPResponse proceedAddItem() {
        if (!checkHeaders("Token") || request.getBody() == null || request.getBody().length == 0) {
            HTTPResponse response = new HTTPResponse(HTTPCode.BAD_REQUEST_400);
            response.setBody("Нужен заголовок Token и новость в теле сообщения!");
            return response;
        }
