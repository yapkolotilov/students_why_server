package studentswhyserver.data.tree;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataTreeNodeTest {

    @Test
    public void getId() {
        DataTreeNode node = new DataTreeNode("bla");
        node.getId();
    }

    @Test
    public void setId() {
        DataTreeNode node = new DataTreeNode("bla");
        node.setId("id");
    }

    @Test
    public void matches() {
        DataTreeNode node = new DataTreeNode("bla");
        node.matches("id");
    }

    @Test
    public void getNodes() {
        DataTreeNode node = new DataTreeNode("bla");
        node.getNodes();
    }

    @Test
    public void getLeaves() {
        DataTreeNode node = new DataTreeNode("bla");
        node.getLeaves();
    }

    @Test
    public void containsNode() {
        DataTreeNode node = new DataTreeNode("bla");
        node.containsNode("d");
    }

    @Test
    public void containsNode1() {
        DataTreeNode node = new DataTreeNode("bla");
        node.containsNode(node);
    }

    @Test
    public void getNode() {
        try {
            DataTreeNode node = new DataTreeNode("bla");
            node.getNode("bla");
        } catch (Exception e) {
        }
    }

    @Test
    public void addNewNode() {
        try {
            DataTreeNode node = new DataTreeNode("bla");
            node.addNewNode("da");
        } catch (Exception e) {

        }
    }

    @Test
    public void addNode() {
        try {
            DataTreeNode node = new DataTreeNode("bla");
            node.addNode(node);
        } catch (Exception e) {

        }
    }

    @Test
    public void removeNode() {
        try {
            DataTreeNode node = new DataTreeNode("bla");
            node.removeNode(node);
        } catch (Exception e) {

        }
    }

    @Test
    public void containsLeaf() {
        try {
            DataTreeNode node = new DataTreeNode("bla");
            node.containsLeaf("da");
        } catch (Exception e) {

        }
    }

    @Test
    public void containsLeaf1() {
        try {
            DataTreeNode node = new DataTreeNode("bla");
            node.containsLeaf(new DataTreeLeaf("a", "d"));
        } catch (Exception e) {

        }
    }

    @Test
    public void getLeaf() {
        try {
            DataTreeNode node = new DataTreeNode("bla");
            node.getLeaf("ida");
        } catch (Exception e) {

        }
    }

    @Test
    public void addNewLeaf() {
        try {
            DataTreeNode node = new DataTreeNode("bla");
            node.addNewLeaf("da", node);
        } catch (Exception e) {

        }
    }

    @Test
    public void addLeaf() {
        try {
            DataTreeNode node = new DataTreeNode("bla");
            node.addLeaf(new DataTreeLeaf("a", "b"));
        } catch (Exception e) {

        }
    }

    @Test
    public void removeLeaf() {
        try {
            DataTreeNode node = new DataTreeNode("bla");
            node.removeLeaf(new DataTreeLeaf("da", "a"));
        } catch (Exception e) {

        }
    }

    @Test
    public void getClone() {
        DataTreeNode node = new DataTreeNode("da");
        node.getClone(Integer.class);
    }

    @Test
    public void getShallowClone() {
        DataTreeNode node = new DataTreeNode("da");
        node.getShallowClone(Integer.class);
    }

    @Test
    public void compareTo() {
        DataTreeNode node = new DataTreeNode("da");
        node.compareTo(node);
    }

    @Test
    public void toString1() {
        DataTreeNode node = new DataTreeNode("da");
        node.toString();
    }
}