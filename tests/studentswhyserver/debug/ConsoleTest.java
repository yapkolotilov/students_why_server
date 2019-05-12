package studentswhyserver.debug;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConsoleTest {

    @Test
    public void print() {
        Console.print("ad");
    }

    @Test
    public void print1() {
        Console.print("bla", "da");
    }

    @Test
    public void println() {
        Console.print("pra");
    }

    @Test
    public void println1() {
        Console.println("da", "dada");
    }

    @Test
    public void println2() {
        Console.println();
    }

    @Test
    public void printlnln() {
        Console.printlnln();
    }

    @Test
    public void printlnln1() {
        Console.printlnln("dada");
    }

    @Test
    public void printlnln2() {
        Console.printlnln("bla", "da");
    }

    @Test
    public void printArray() {
        Console.printArray(new Integer[1]);
    }

    @Test
    public void printlnArray() {
        Console.printlnArray(new Integer[1]);
    }
}