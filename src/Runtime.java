package org.kotlin.spring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.kotlin.spring.ParseTree.parseTree;


public class Runtime {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide a file path as the first argument.");
            return;
        }

        String filePath = args[0];
        String line = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            if ((line = reader.readLine()) == null) {
                throw new IOException();
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file:");
            System.exit(1);
        }
        Node parseTree = parseTree(line);
        List<String> arguments = new ArrayList<>();
        arguments.add("15");
        arguments.add("22");
        Environment environment = new Environment(parseTree, arguments);
        environment.runProgram(null);
    }
}
