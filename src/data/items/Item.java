package data.items;

import java.io.Serializable;

/** Представляет новость или часто задаваемый вопрос.
 *
 */
public class Item implements Serializable {
    static final long serialVersionUID = 12L;
    private String header; // Заголовок новости.
    private String content; // Контент новости.
    private String publishDate; // Дата публикации.

    public String getHeader() {
        return header;
    }

    public boolean matches(String header) {
        return header.equals(header);
    }

    public boolean matches(Class clazz) {
        return getClass() == clazz;
    }

    public boolean matches(String header, Class clazz) {
        return matches(header) && matches(clazz);
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        return String.format("header: %s, content: %s", header, content);
    }
}
