package studentswhyserver.data.programs;

import studentswhyserver.utilities.GlobalMethods;

/** Представляет образовательную дисциплину.
 *
 */
public class Discipline {
    static final long serialVersionUID = 31L;

    private String name; // Название дисциплины.
    private String type; // Тип дисциплины.
    private String[] tutors; // Преподаватели.
    private String schedule; // Время преподавания.

    private String url; // Ссылка на страницу дисциплины.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTutors(String[] tutors) {
        this.tutors = tutors;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return String.format("name: %s, type: %s, tutors: %s, schedule: %s, url: %s", name, type,
                GlobalMethods.arrayToString(tutors), schedule, url);
    }
}
