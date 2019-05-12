package studentswhyserver.data.tree;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataTreeLeafTest {

    @Test
    public void setID() {
        DataTreeLeaf leaf = new DataTreeLeaf("bla", "bla");
        leaf.setID("bl");
    }

    @Test
    public void setValue() {
        DataTreeLeaf leaf = new DataTreeLeaf("bla", "bla");
        leaf.setValue("dadad");
    }

    @Test
    public void getValue() {
        DataTreeLeaf leaf = new DataTreeLeaf("bla", "bla");
        leaf.getValue();
    }

    @Test
    public void getID() {
        DataTreeLeaf leaf = new DataTreeLeaf("bla", "bla");
        leaf.getID();
    }

    @Test
    public void matches() {
        DataTreeLeaf leaf = new DataTreeLeaf("bla", "bla");
        leaf.matches(Integer.class);
    }

    @Test
    public void matches1() {
        DataTreeLeaf leaf = new DataTreeLeaf("bla", "bla");
        leaf.matches("id", Integer.class);
    }

    @Test
    public void compareTo() {
        DataTreeLeaf leaf = new DataTreeLeaf("bla", "bla");
        leaf.compareTo(new DataTreeLeaf("a", "b"));
    }

    @Test
    public void getClone() {
        DataTreeLeaf leaf = new DataTreeLeaf("bla", "bla");
        leaf.getClone();
    }

    @Test
    public void toString1() {
        DataTreeLeaf leaf = new DataTreeLeaf("bla", "bla");
        leaf.toString();
    }

    @Test
    public void toStringFormat() {
        DataTreeLeaf leaf = new DataTreeLeaf("bla", "bla");
        leaf.toStringFormat(" ");
    }
}