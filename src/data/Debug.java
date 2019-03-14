package data;

import data.programs.ProgramTree;
import debug.Console;

import java.io.Serializable;

public class Debug {
    public static void main(String[] args) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static class SerObject implements Serializable {
        private SerObject other;
    }
}
