package org.kotlin.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseTree {
    private static final String PATTERN = "^([a-zA-Z0-9_]+)\\((.*)\\)$";
    public static Node parseTree(String parseTree) {
        Node node = new Node(getName(parseTree, PATTERN));
        String innerTree = findInnerNode(parseTree);
        List<Node> params = new ArrayList<>();
        node.setParameters(params);

        while (!innerTree.isEmpty()) {
            if (innerTree.charAt(0) == '(') {
                innerTree = findInnerNode(innerTree);
            }

            int i = findNextNode(innerTree);

            if (i == 0) {
                node = new Node("value");
                node.setValue(innerTree);
                return node;
            }

            if (i < innerTree.length()) {
                String branch = innerTree.substring(0,i);
                params.add(parseTree(branch));

                innerTree = innerTree.substring(i);
                if (innerTree.charAt(0) == ',') {
                    innerTree = innerTree.substring(1);
                }
            } else {
                params.add(parseTree(innerTree));
                break;
            }
        }

        return node;
    }

    public static String getName(String parseTree, String patternString) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(parseTree);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return parseTree;
        }
    }

    public static int findNextNode(String expression) {
        Stack<Integer> stack = new Stack<>();
        boolean openingStart = false;
        int closingIndex = 0;
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == '(') {
                if (!openingStart) {
                    openingStart = true;
                }

                stack.push(i);
            }
            else if (ch == ')') {
                if (!openingStart) {
                    continue;
                }
                stack.pop();
                closingIndex = i + 1;
                if (stack.isEmpty()) {
                    return closingIndex;
                }
            }
        }

        if (stack.isEmpty()) {
            return closingIndex;
        } else {
            return 0;
        }
    }

    public static String findInnerNode(String expression) {
        Stack<Integer> stack = new Stack<>();
        boolean openingStart = false;
        int openingIndex = 0;
        int closingIndex = 0;
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == '(') {
                if (!openingStart) {
                    openingStart = true;
                    openingIndex = i + 1;
                }

                stack.push(i);
            }
            else if (ch == ')') {
                if (!openingStart) {
                    continue;
                }
                stack.pop();
                closingIndex = i;
                if (stack.isEmpty()) {
                    return expression.substring(openingIndex, i);
                }
            }
        }

        if (stack.isEmpty()) {
            return expression.substring(openingIndex, closingIndex);
        } else {
            return "";
        }
    }
}
