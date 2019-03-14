package data.personalData;

import debug.Console;
import exceptions.DuplicateElemException;
import exceptions.NoSuchElemException;
import utilities.FilePath;

import java.io.*;
import java.util.LinkedList;

/** Представляет список логинов и паролей пользователей.
 *
 */
public class PersonalDataList implements Serializable {
    static final long serialVersionUID = 22L;

    private  FilePath path; // Путь к файлу.
    LinkedList<PersonalData> personalDatas = new LinkedList<>(); // Пароли.
    public static final PersonalData sudoData = new PersonalData("sudoLogin", "sudoPassword", generateToken("sudoLogin", "sudoPassword"));

    private PersonalDataList(FilePath path) {
        this.path = path;
    }


    // --- Работа с файлами ---

    /** Сохраняет пароли в файл.
     *
     */
    private void saveToFile() {
        try {
            PrintWriter writer = new PrintWriter(path.toString());
            for (PersonalData eachPersonalData : personalDatas)
                writer.println(eachPersonalData);
            writer.close();
        } catch (IOException e ) {
            debug.Console.println("ОШИБКА ПРИ СОХРАНЕНИИ ПАРОЛЕЙ");
            e.printStackTrace();
        }
    }

    /** Считывает пароли из файла.
     *
     * @param path Путь к файлу.
     * @return пароли из файла.
     */
    public static PersonalDataList readFromFile(FilePath path) {
        File file = new File(path.toString());

        try {
            if (!file.exists()) {
                file.createNewFile();
                return new PersonalDataList(path);
            }

            PersonalDataList result = new PersonalDataList(path);
            BufferedReader reader = new BufferedReader(new FileReader(path.toString()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.personalDatas.add(PersonalData.parseString(line));
            }
            reader.close();
            return result;
        } catch (IOException e) {
            Console.println("Ошибка при чтении файла!");
            e.printStackTrace();
            return new PersonalDataList(path);
        }
    }


    // --- Работа с паролями ---

    /** Получает персональные данные по логину.
     *
     * @param login Логин.
     * @return персональные данные по логину.
     */
    public PersonalData getByLogin(String login) throws NoSuchElemException {
        if (login.equals(sudoData.getLogin()))
            return sudoData;

        for (PersonalData eachData: personalDatas)
            if (eachData.getLogin().equals(login))
                return eachData;

        throw new NoSuchElemException("Нет персональных данных для данного логина!");
    }

    /** Получает персональные данные по токену.
     *
     * @param token Токен.
     * @return персональные данные по токену.
     */
    public PersonalData getByToken(String token) throws NoSuchElemException {
        if (token.equals(sudoData.getToken()))
            return sudoData;
        for (PersonalData eachData: personalDatas)
            if (eachData.getToken().equals(token))
                return eachData;

        throw new NoSuchElemException("Нет персональных данных для данного токена!");
    }

    /** Проверяет, содержит ли база паролей указанный логин.
     *
     * @param login Логин.
     * @return содержит ли база паролей указанный логин.
     */
    public boolean containsLogin(String login) {
        if (login.equals(sudoData.getLogin()))
            return true;

        for (PersonalData eachData: personalDatas)
            if (eachData.getLogin().equals(login))
                return true;

        return false;
    }

    /** Добавляет нового пользователя.
     *
     * @param login Логин.
     * @param password Пароль.
     * @return Токен авторизации.
     */
    public String add(String login, String password) throws DuplicateElemException {
        if (login.equals(sudoData.getLogin()) || containsLogin(login))
            throw new DuplicateElemException("Данный пользователь уже зарегистрирован в системе!");

        String token = generateToken(login, password);
        personalDatas.add(new PersonalData(login, password, token));

        saveToFile();
        return token;
    }

    /** Удаляет персональные данные.
     *
     * @param token Токен.
     */
    public void remove(String token) throws NoSuchElemException {
        if (token.equals(sudoData.getToken()))
            return;

        PersonalData data = getByToken(token);
        personalDatas.remove(data);

        saveToFile();
    }

    /** Проверяет логин и пароль.
     *
     * @param login Логин.
     * @param password Пароль.
     * @return Правильность логина и пароля.
     */
    public boolean validate(String login, String password) {
        if (generateToken(login, password).equals(sudoData.getToken()))
            return true;

        for (PersonalData eachData: personalDatas)
            if (eachData.matches(login, password))
                return true;

        return false;
    }

    /** Проверяет токен.
     *
     * @param token Токен.
     * @return Правильность токена.
     */
    public boolean validate(String token) {
        if (token.equals(sudoData.getToken()))
            return true;

        for (PersonalData eachData: personalDatas)
            if (eachData.matches(token))
                return true;

        return false;
    }

    public static boolean validateSudo(String token) {
        return token.equals(sudoData.getToken());
    }

    public static boolean validateSudo(String login, String password) {
        return validateSudo(generateToken(login, password));
    }

    // --- Токены ---

    /** Генерирует строковый токен для логина и пароля.
     *
     * @param login Логин.
     * @param password Пароль.
     * @return строковый токен для логина и пароля.
     */
    static String generateToken(String login, String password) {
        int result = 7;
        String content = login + "_" + password;
        for (int i = 0; i < content.length(); i++) {
            result = result*31 + content.charAt(i);
        }
        return new Integer(result).toString();
    }
}
