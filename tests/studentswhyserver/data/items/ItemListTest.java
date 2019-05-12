package studentswhyserver.data.items;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import studentswhyserver.utilities.FilePath;

import static org.junit.Assert.*;

public class ItemListTest {

    @Test
    public void readFromFile() {
        ItemList.readFromFile(new FilePath());
    }

    @Test
    public void getItem() {
        ItemList itemList = ItemList.readFromFile(new FilePath());
        try {
            itemList.getItem("ad");
        } catch (Exception e) {
        }
    }

    @Test
    public void getEvent() {
        ItemList itemList = ItemList.readFromFile(new FilePath());
        try {
            itemList.getEvent("ad");
        } catch (Exception e) {
        }
    }

    @Test
    public void contains() {
        ItemList itemList = ItemList.readFromFile(new FilePath());
        assertFalse(itemList.contains("daad"));
    }

    @Test
    public void add() {
        ItemList itemList = ItemList.readFromFile(new FilePath("test.items"));
        try {
            itemList.add(new Item());
        } catch (Exception e) {

        }
    }

    @Test
    public void change() {
        ItemList itemList = ItemList.readFromFile(new FilePath("test.items"));
        try {
            itemList.change("blabla", new Item());
        } catch (Exception  e) {

        }
    }

    @Test
    public void remove() {
        ItemList itemList = ItemList.readFromFile(new FilePath("test.items"));
        try {
            itemList.change("blabla", new Item());
        } catch (Exception  e) {

        }
    }

    @Test
    public void getItemsJson() {
        ItemList itemList = ItemList.readFromFile(new FilePath("test.items"));
        itemList.getItemsJson();
    }

    @Test
    public void getLastItemsJson() {
        ItemList itemList = ItemList.readFromFile(new FilePath("test.items"));
        itemList.getLastItemsJson(13);
    }

    @Test
    public void getEventsJson() {
        ItemList itemList = ItemList.readFromFile(new FilePath("test.items"));
        itemList.getEventsJson();
    }

    @Test
    public void getLastEventsJson() {
        ItemList itemList = ItemList.readFromFile(new FilePath("test.items"));
        itemList.getLastEventsJson(1);
    }

    @Test
    public void toString1() {
        ItemList itemList = ItemList.readFromFile(new FilePath("test.items"));
        itemList.toString();
    }
}