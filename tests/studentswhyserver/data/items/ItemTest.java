package studentswhyserver.data.items;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {

    @Test
    public void getHeader() {
        Item item = new Item();
        item.setHeader("blabla");
        Assert.assertEquals(item.getHeader(), "blabla");
    }

    @Test
    public void matches() {
        Item item = new Item();
        Assert.assertFalse(item.matches(Integer.class));
    }

    @Test
    public void matches1() {
        Item item = new Item();
        item.setHeader("blabla");
        Assert.assertFalse(item.matches("blbla"));
    }

    @Test
    public void matches2() {
        Item item = new Item();
        item.setHeader("blabla");
        Assert.assertTrue(item.matches("blabla", Item.class));
    }

    @Test
    public void setHeader() {
        Item item = new Item();
        item.setHeader("blabla");
        Assert.assertEquals(item.getHeader(), "blabla");
    }

    @Test
    public void setContent() {
        Item item = new Item();
        item.setContent("blabla");
    }

    @Test
    public void setPublishDate() {
        Item item = new Item();
        item.setPublishDate("01.01.2019");
    }

    @Test
    public void like() {
        Item item = new Item();
        item.like();
    }

    @Test
    public void dislike() {
    }

    @Test
    public void toString1() {
        Assert.assertEquals(new Item().toString(), "header: null, content: null");
    }
}