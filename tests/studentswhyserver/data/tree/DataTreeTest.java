package studentswhyserver.data.tree;

import org.junit.Test;

import javax.xml.crypto.Data;

import static org.junit.Assert.*;

public class DataTreeTest {

    @Test
    public void getRoot() {
        DataTree tree = new DataTree("root");
        tree.getRoot();
    }

    @Test
    public void containsNode() {
        DataTree tree = new DataTree("root");
        tree.containsNode("bla");
    }

    @Test
    public void containsNode1() {
        DataTree tree = new DataTree("root");
        tree.containsNode(new DataTreeNode("bla"));
    }

    @Test
    public void getNode() {
        try {
            DataTree tree = new DataTree("root");
            tree.getNode("bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void getParent() {
        try {
            DataTree tree = new DataTree("root");
            tree.getParent(new DataTreeNode("bla"));
        } catch (Exception e) {

        }
    }

    @Test
    public void addNewNode() {
        try {
            DataTree tree = new DataTree("root");
            tree.addNewNode("id", new DataTreeNode("bla"));
        } catch (Exception e) {

        }
    }

    @Test
    public void setParent() {
        try {
            DataTree tree = new DataTree("root");
            tree.setParent(new DataTreeLeaf("bla", 1), new DataTreeNode("bla"));
        } catch (Exception e) {

        }
    }

    @Test
    public void removeNode() {
        try {
            DataTree tree = new DataTree("root");
            tree.removeNode(new DataTreeNode("bla"));
        } catch (Exception e) {

        }
    }

    @Test
    public void changeNode() {
        try {
            DataTree tree = new DataTree("root");
            tree.changeNode(new DataTreeNode("bla"), "bla");
        } catch (Exception e) {

        }
    }

    @Test
    public void containsLeaf() {
        try {
            DataTree tree = new DataTree("root");
            tree.containsLeaf("bla", Integer.class);
        } catch (Exception e) {

        }
    }

    @Test
    public void containsLeaf1() {
        try {
            DataTree tree = new DataTree("root");
            tree.containsLeaf(new DataTreeLeaf("bla", "bla"));
        } catch (Exception e) {

        }
    }

    @Test
    public void getLeaf() {
        try {
            DataTree tree = new DataTree("root");
            tree.getLeaf("id", Integer.class);
        } catch (Exception e) {

        }
    }

    @Test
    public void getAllParents() {
        try {
            DataTree tree = new DataTree("root");
            tree.getAllParents(new DataTreeLeaf("b", "b"));
        } catch (Exception e) {

        }
    }

    @Test
    public void addNewLeaf() {
        try {
            DataTree tree = new DataTree("root");
            tree.addNewLeaf("id", new DataTreeLeaf("bla", "bla"), new DataTreeNode("bla"));
        } catch (Exception e) {

        }
    }

    @Test
    public void hasParent() {
        try {
            DataTree tree = new DataTree("root");
            tree.hasParent(new DataTreeLeaf("", ""), new DataTreeNode("bla"));
        } catch (Exception e) {

        }
    }

    @Test
    public void setParent1() {
        try {
            DataTree tree = new DataTree("root");
            tree.setParent(new DataTreeLeaf("leaf", "leaf"), new DataTreeNode("bla"));
        } catch (Exception e) {

        }
    }

    @Test
    public void addParent() {
        try {
            DataTree tree = new DataTree("root");
            tree.addParent(new DataTreeLeaf("leaf", "leaf"), new DataTreeNode("bla"));
        } catch (Exception e) {

        }
    }

    @Test
    public void removeParent() {
        try {
            DataTree tree = new DataTree("root");
            tree.removeParent(new DataTreeLeaf("leaf", "leaf"), new DataTreeNode("bla"));
        } catch (Exception e) {

        }
    }

    @Test
    public void removeLeaf() {
        try {
            DataTree tree = new DataTree("root");
            tree.removeLeaf(new DataTreeLeaf("bl", "da"));
        } catch (Exception e) {

        }
    }

    @Test
    public void changeLeaf() {
        try {
            DataTree tree = new DataTree("root");
            tree.changeLeaf(new DataTreeLeaf("", "da"), "bla", new DataTreeLeaf("bla", "da"));
        } catch (Exception e) {

        }
    }

    @Test
    public void getClone() {
        try {
            DataTree tree = new DataTree("root");
            tree.getClone(Integer.class);
        } catch (Exception e) {

        }
    }

    @Test
    public void toString1() {
        try {
            DataTree tree = new DataTree("root");
            tree.toString();
        } catch (Exception e) {

        }
    }
}