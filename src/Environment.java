package org.kotlin.spring;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Environment {
    private Node parseTree;
    private HashMap<String, Variable> variables;
    private List<Node> commands;
    private List<String> inputs;
    List<String> booleanOperators = Arrays.asList("==", ">", ">=", "<", "<=", "&&", "||", "and", "or");

    public Environment(Node parseTree, List<String> inputs) {
        this.parseTree = parseTree;
        this.variables = new HashMap<>();
        this.inputs = inputs;
        Node blockNode = parseProgram();
        if (blockNode == null) {
            System.err.println("Could not find the function body");
            System.exit(1);
        }
        this.commands = blockNode.getParameters();
    }

    private Node parseProgram() {
        if (parseTree == null) {
            return null;
        }

        if (checkFalseNodeName(parseTree, "program"))
            return null;

        Node funcNode = parseTree.getParameters().get(0);
        if (checkFalseNodeName(funcNode, "func"))
            return null;

        List<Node> params = funcNode.getParameters();
        Node signatureNode = params.get(0);
        Node blockNode = params.get(1);
        if (checkFalseNodeName(signatureNode, "sign") && checkFalseNodeName(blockNode, "block"))
            return null;

        params = signatureNode.getParameters();
        Node returnTypeNode = params.get(0);
        Node funcNameNode = params.get(1);
        List<Node> argumentNodeList = params.get(2).getParameters();
        if (checkFalseNodeName(returnTypeNode, "value"))
            return null;
        if (checkFalseNodeName(funcNameNode, "value"))
            return null;
        if (checkFalseListForArguments(argumentNodeList))
            return null;
        loadArguments(argumentNodeList);

        return blockNode;
    }

    public boolean runProgram(List<Node> commands) {

        if (commands == null) {
            commands = this.commands;
        }

        for (Node command : commands) {
            switch (command.getName()) {
                case "decl":
                    declarations(command);
                    break;
                case "assign":
                    assign(command);
                    break;
                case "cond":
                    conditional(command);
                    break;
                default:
                    return false;
            }
        }

        for (Variable variable : variables.values()) {
            System.out.println(variable.toString());
        }
        return true;
    }

    public void conditional(Node node) {
        Node unwrappedNode = node.getParameters().get(0);
        switch (unwrappedNode.getName()) {
            case "ifte":
                ifte(unwrappedNode);
                break;
            case "wloop":
                wloop(unwrappedNode);
            default:
                break;
        }
    }

    public boolean booleanExpression(Node node) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Node> params = node.getParameters();
        for (Node param : params) {
            String value = param.getValue();
            if (booleanOperators.contains(value)) {
                stringBuilder.append(value);
            } else {
                int i = 0;
                boolean notNumber = false;
                if (value.charAt(0) == '-') {
                    if (value.length() == 1)
                        return false;
                    i = 1;
                }

                for (; i < value.length(); i++) {
                    if (!Character.isDigit(value.charAt(i))) {
                        notNumber = true;
                        break;
                    }
                }
                if (notNumber) {
                    stringBuilder.append(variables.get(value).getValue());
                } else {
                    stringBuilder.append(value);
                }
            }
        }
        String expression = stringBuilder.toString();
        return BooleanEvaluator.booleanEval(expression);
    }

    public void ifte(Node node) {
        List<Node> nodes = node.getParameters();
        Node condParan = nodes.get(0);
        Node block = nodes.get(1);
        if (checkFalseNodeName(condParan, "condParan"))
            System.err.println("Expected Parse Tree to contain: condParan");
        if (checkFalseNodeName(block, "block"))
            System.err.println("Expected Parse Tree to contain: block");
        boolean condition = booleanExpression(condParan.getParameters().get(0));
        if (condition) {
            runProgram(block.getParameters());
        } else if (nodes.size() == 3) {
            if (nodes.get(2).getName().equals("elseif")) {
                conditional(nodes.get(2));
            } else if (nodes.get(2).getName().equals("else")) {
                runProgram(nodes.get(2).getParameters());
            }
        }
    }

    public void wloop(Node node) {
        List<Node> nodes = node.getParameters();
        Node condParan = nodes.get(0);
        Node block = nodes.get(1);
        boolean condition = booleanExpression(node.getParameters().get(0).getParameters().get(0));
        while (condition) {
            runProgram(block.getParameters().get(1).getParameters());
            condition = booleanExpression(condParan.getParameters().get(0));
        }
    }

    public void declarations(Node node) {
        String name = node.getParameters().get(1).getValue();
        if (variables.containsKey(name)) {
            System.err.println("Variable " + name + " was already declared");
            System.exit(1);
        }

        loadVariable(node, node.getParameters().get(2).getValue());

    }

    public void assign(Node node) {
        List<Node> parameters = node.getParameters();
        String name = parameters.get(0).getValue();
        if (!variables.containsKey(name)) {
            System.err.println("Variable " + name + " was not declared");
            System.exit(1);
        }

        Variable variable = variables.get(name);
        String value = parameters.get(1).getValue();
        switch (variable.getType()) {
            case "int":
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid assignment for variable: " + name + ", expected type int");
                    System.exit(1);
                }
                break;
            case "string":
                if (!(value.substring(0, 1).equals("\"")
                        && value.substring(value.length() - 1, value.length()).equals("\""))) {
                    System.err.println("Invalid assignment for variable: " + name + ", expected type string");
                    System.exit(1);
                }
                break;
            case "boolean":
                if (variable.getValue().equals("true")
                        || variable.getValue().equals("false")
                        || variable.getValue().equals("null")) {
                    System.err.println("Invalid assignment for variable: " + name + ", expected type boolean");
                    System.exit(1);
                }
                break;
        }

        variable.setValue(value);
    }

    public void loadArguments(List<Node> nodes) {
        if (inputs.size() != nodes.size()) {
            System.err.println("Expected " + variables.size() + " but found " + nodes.size());
            System.exit(1);
        }

        int index = 0;
        for (Node argument : nodes) {
            loadVariable(argument, inputs.get(index++));
        }
    }

    public void loadVariable(Node node, String value) {
        List<Node> params = node.getParameters();
        Variable variable = new Variable(params.get(1).getValue(), params.get(0).getValue(), value);
        variables.put(params.get(1).getValue(), variable);
    }

    public boolean checkFalseListForArguments(List<Node> list) {
        for (Node argument : list) {
            if (checkFalseNodeName(argument, "argv"))
                return true;
            List<Node> params = argument.getParameters();
            for (Node param : params) {
                if (checkFalseNodeName(param, "value"))
                    return true;
            }
        }

        return false;
    }

    public boolean checkFalseNodeName(Node node, String name) {
        boolean check = !node.getName().equals(name);
        if (check) {
            System.err.println("Expected Parse Tree to contain: " + name);
            System.exit(1);
        }
        return check;
    }
}
