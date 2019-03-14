package server;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import data.items.Event;
import data.items.Item;
import data.personalData.PersonalData;
import data.personalData.PersonalDataList;
import data.programs.Discipline;
import data.programs.Tutor;
import data.programs.User;
import data.tree.DataTreeNode;
import enums.HTTPCode;
import enums.Result;
import exceptions.*;
import http.HTTPRequest;
import http.HTTPResponse;
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
