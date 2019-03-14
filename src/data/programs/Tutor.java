package data.programs;

import utilities.GlobalMethods;

import java.io.Serializable;

/** Представляет преподавателя.
 *
 */
public class Tutor implements Serializable {
    static final long serialVersionUID = 33L;

    private String name; // ФИО.
    private String[] emails; // Почты.
    private String[] phones; // Телефоны.

    private String[] disciplines; // Дисциплины.
    private String[] posts; // Должности.

    private String adress; // Кабинет с адресом.
    private String attendTime; // Время присутствия.

    private String url; // Ссылка на личную страницу.
    private String imgURL; // Ссылка на иконку.

    public Tutor() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhones(String... phones) {
        this.phones = phones;
    }

    public void setEmails(String... emails) {
        this.emails = emails;
    }

    public void setDisciplines(String... disciplines) {
        this.disciplines = disciplines;
    }

    public void setPosts(String... posts) {
        this.posts = posts;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @Override
    public String toString() {
        return String.format(("name: %s, emails: %s, phones: %s, disciplines: %s, posts: %s, adress: %s, attendTime: %s," +
                        " url: %s, imgURL: %s"), name, GlobalMethods.arrayToString(emails), GlobalMethods.arrayToString(phones),
                GlobalMethods.arrayToString(disciplines), GlobalMethods.arrayToString(posts), adress, attendTime, url, imgURL);
    }
}
