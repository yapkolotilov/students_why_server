package studentswhyserver.data.items;

import org.junit.Test;
import studentswhyserver.utilities.FilePath;

import static org.junit.Assert.*;

public class StudentsWhyTreeTest {

    StudentsWhyTree tree = StudentsWhyTree.readFromFile(new FilePath("test.tree"));

    @Test
    public void saveToFile() {
        tree.saveToFile();
    }

    @Test
    public void readFromFile() {
        tree = StudentsWhyTree.readFromFile(new FilePath("test.txt"));
    }

    @Test
    public void getTag() {
        try {
            tree.getTag("blabla");
        }catch (Exception e) {

        }
    }

    @Test
    public void addNewTag() {
        try {
            tree.addNewTag("blabla");
        }catch (Exception e) {

        }
    }

    @Test
    public void addNewTag1() {
        try {
            tree.addNewTag("blabla", "blabla");
        }catch (Exception e) {

        }
    }

    @Test
    public void rebaseTag() {
        try {
            tree.rebaseTag("bla", "bla");
        }catch (Exception e) {

        }
    }

    @Test
    public void removeTag() {
        try {
            tree.removeTag("bla");
        }catch (Exception e) {

        }
    }

    @Test
    public void changeTag() {
        try {
            tree.changeTag("bla", "blabla");
        }catch (Exception e) {

        }
    }

    @Test
    public void getQuestion() {
        try {
            tree.getQuestion("blabla");
        }catch (Exception e) {

        }
    }

    @Test
    public void addNewQuestion() {
        try {
            tree.addNewQuestion("bla", new Item());
        }catch (Exception e) {

        }
    }

    @Test
    public void addNewQuestion1() {
        try {
            tree.addNewQuestion("bla", new Item(), "bla");
        }catch (Exception e) {

        }
    }

    @Test
    public void addQuestionToTag() {
        try {
            tree.addQuestionToTag("bla", "blabla");
        }catch (Exception e) {

        }
    }

    @Test
    public void removeQuestionFromTag() {
        try {
            tree.removeQuestionFromTag("bla", "bla");
        }catch (Exception e) {

        }
    }

    @Test
    public void rebaseQuestion() {
        try {
            tree.rebaseQuestion("bla", "bla");
        }catch (Exception e) {

        }
    }

    @Test
    public void removeQuestion() {
        try {
            tree.removeQuestion("bla");
        }catch (Exception e) {

        }
    }

    @Test
    public void changeQuestion() {
        try {
            tree.changeQuestion("bla", new Item());
        }catch (Exception e) {

        }
    }

    @Test
    public void getTagsJson() {
        tree.getTagsJson();
    }

    @Test
    public void getQuestionsJson() {
        tree.getQuestionsJson();
    }

    @Test
    public void getQuestionsTagsJSON() {
        try {
            tree.getQuestionsTagsJSON("bla");
        } catch (Exception e) {

        }
    }
}