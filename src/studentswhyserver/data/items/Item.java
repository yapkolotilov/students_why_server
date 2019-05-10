package studentswhyserver.data.items;

import studentswhyserver.exceptions.NoSuchElemException;

import java.io.Serializable;

/** Представляет новость или часто задаваемый вопрос.
 *
 */
public class Item implements Serializable {
    static final long serialVersionUID = 12L;
    private String header; // Заголовок новости.
    private String content; // Контент новости.
    private String publishDate; // Дата публикации.
    private int rating;

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

    public void setHeader(String header) {
        this.header = header;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void like() {
        rating++;
    }

    public void dislike() throws NoSuchElemException {
        if (rating == 0)
            throw new NoSuchElemException("0 likes!");
        rating--;
    }

    @Override
    public String toString() {
        return String.format("header: %s, content: %s", header, content);
    }
}
