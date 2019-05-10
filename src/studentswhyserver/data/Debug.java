package studentswhyserver.data;

import com.google.gson.Gson;
import studentswhyserver.data.items.Event;
import studentswhyserver.data.items.Item;
import studentswhyserver.data.programs.Discipline;
import studentswhyserver.data.programs.Tutor;

import java.util.Queue;

public class Debug {
    public static void main(String[] args) {
        Gson gson = new Gson();

        Item item = new Item();
        item.setHeader("Something happened?");
        item.setContent("Yep, i found gf");

        System.out.println(gson.toJson(item));
    }
}
