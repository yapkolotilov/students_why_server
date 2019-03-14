package data.items;

/** Представляет событие.
 *
 */
public class Event extends Item {
    static final long serialVersionUID = 11L;
    private String date; // Дата проведения.

    private double latitude; // Широта.
    private double longitude; // Долгота.
    private String place; // Дополнительная информация.

    @Override
    public String toString() {
        return super.toString() + String.format("date: %s, coords: %sx%s, place: %s", date, latitude, longitude,place);
    }
}
