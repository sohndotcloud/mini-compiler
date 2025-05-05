import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Environment {
    private Node parseTree;
    private HashMap<String, Variable> variables;
    private List<Node> commands;
    private List<String> inputs;
    private String returnType;
    private boolean hasReturn;
    private ExpressionEvaluator expressionEvaluator;
    List<String> booleanOperators = Arrays.asList("==", ">", ">=", "<", "<=", "&&", "||", "and", "or");
    List<String> arithmeticOperators = Arrays.asList("add", "subt", "mult", "div");

    public Environment(Node parseTree, List<String> inputs) {
        this.parseTree = parseTree;
        this.variables = new HashMap<>();
        this.inputs = inputs;
        Node blockNode = parseProgram();
        if (blockNode == null) {
            System.err.println("Could not find the function body");
            System.exit(1);
        }
        this.expressionEvaluator = new ExpressionEvaluator(variables);
        this.commands = blockNode.getParameters();
        this.hasReturn = false;
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
        this.returnType = params.get(0).getValue();
        if (params.size() == 3){
            List<Node> argumentNodeList = params.get(2).getParameters();
            if (checkFalseListForArguments(argumentNodeList))
                return null;
            loadArguments(argumentNodeList);
        }

        return blockNode;
    }

    public boolean runProgram(List<Node> commands) {
        boolean nested = true;
        if (commands == null) {
            commands = this.commands;
            nested = false;
        }

        for (Node command : commands) {
            if (this.hasReturn) {
                break;
            }

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
                case "return":
                    returnCommand(command);
                    break;
                case "print_id":
                    printId(command);
                    break;
                case "print":
                case "print_string":
                    print(command);
                    break;
                default:
                    return false;
            }
        }

        if (!this.hasReturn && !nested) {
            System.out.println("Error: no return found of type: " + returnType);
        }

        return true;
    }

    public Variable getVariable(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }

        return null;
    }

    public void print(Node node) {
        System.out.println("PRINT - " + node.getParameters().get(0).getValue());
    }

    public void printId(Node node) {
        String id = node.getParameters().get(0).getValue();
        if (id.equals("true") || id.equals("false")) {
            System.out.println("PRINT - " + node.getParameters().get(0).getValue());
        }

        if (getVariable(id) != null) {
            System.out.println("PRINT - " + getVariable(id).getValue());
        }
    }

    public void returnCommand(Node command) {
        Node expressionNode = command.getParameters().get(0);
        switch (this.returnType) {
            case "int":
                int output = (int) expressionEvaluator.evaluateExpression(expressionNode);
                System.out.println("Return value: " + output);
                break;
            case "boolean":
                boolean result = booleanExpression(expressionNode);
                System.out.println("Return value: " + result);
            case "string":
                if (this.returnType.equals("string")) {
                    if (variables.containsKey(expressionNode.getValue())) {
                        System.out.println("Return value: " + variables.get(expressionNode.getValue()).getValue());
                    } else {
                        System.out.println("Return value: " + expressionNode.getValue());
                    }
                }
        }
        this.hasReturn = true;
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
        if (params != null) {
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
                        if (variables.containsKey(value)) {
                            stringBuilder.append(variables.get(value).getValue());
                        } else {
                            stringBuilder.append(value);
                        }
                    } else {
                        stringBuilder.append(value);
                    }
                }
            }
        } else {
            stringBuilder.append(node.getValue());
        }
        String expression = stringBuilder.toString();
        return BooleanEvaluator.booleanEval(expression);
    }

    public void ifte(Node node) {
        List<Node> nodes = node.getParameters();
        Node condParan = nodes.get(0);
        Node block = nodes.get(1);
        boolean condition = booleanExpression(condParan.getParameters().get(0));
        if (condition) {
            runProgram(block.getParameters());
        } else if (nodes.size() == 3) {
            if (nodes.get(2).getName().equals("elseif")) {
                conditional(nodes.get(2));
            } else if (nodes.get(2).getName().equals("else")) {
                runProgram(nodes.get(2).getParameters().get(0).getParameters());
            }
        }
    }

    public void wloop(Node node) {
        List<Node> nodes = node.getParameters();
        Node condParan = nodes.get(0);
        Node block = nodes.get(1);
        boolean condition = booleanExpression(node.getParameters().get(0).getParameters().get(0));
        while (condition) {
            runProgram(block.getParameters());
            condition = booleanExpression(condParan.getParameters().get(0));
        }
    }

    public void declarations(Node node) {
        String name = node.getParameters().get(1).getValue();
        if (variables.containsKey(name)) {
            System.err.println("Variable " + name + " was already declared");
            System.exit(1);
        }

        if (node.getParameters().size() == 2) {
            loadVariable(node, "");
        } else {
            loadVariable(node,  node.getParameters().get(2).getValue());
        }
    }

    public void assign(Node node) {
        List<Node> parameters = node.getParameters();
        String name = parameters.get(0).getValue();
        if (!variables.containsKey(name)) {
            System.err.println("Variable " + name + " was not declared");
            System.exit(1);
        }

        Variable variable = variables.get(name);
        String value = variable.getValue();

        switch (variable.getType()) {
            case "int":
                if (parameters.size() == 2
                        && parameters.get(1).getName().equals("value")) {
                    if (parameters.get(1).getValue().equals("++")
                            || parameters.get(1).getValue().equals("--")) {
                        expressionEvaluator.evaluateExpression(node);
                        return;
                    } else if (parameters.get(0).getValue().equals("++")
                            || parameters.get(0).getValue().equals("--")) {
                        expressionEvaluator.evaluateExpression(parameters.get(1));
                        return;
                    } else {
                        value = parameters.get(1).getValue();
                    }
                } else if (parameters.get(1).getName().equals("tern")) {
                    List<Node> list = parameters.get(1).getParameters();
                    boolean result = booleanExpression(list.get(0));
                    if (result) {
                        value = list.get(1).getValue();
                    } else {
                        value = list.get(2).getValue();
                    }
                    variable.setValue(value);
                    return;
                }

                try {
                    if (parameters.get(1).getValue() != null) {
                        Integer.parseInt(parameters.get(1).getValue());
                    } else {
                        value = String.valueOf((int) expressionEvaluator.evaluateExpression(node.getParameters().get(1)));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid assignment for variable: " + name + ", expected type int");
                    System.exit(1);
                }
                break;
            case "string":
                value = parameters.get(1).getValue();
                break;
            case "boolean":
                if (parameters.get(1).getName().equals("tern")) {
                    value = String.valueOf(booleanExpression(parameters.get(1).getParameters().get(0)));
                    if (value.equals("true")) {
                        variable.setValue(parameters.get(1).getParameters().get(0).getValue());
                    } else if (value.equals("false")) {
                        variable.setValue(parameters.get(1).getParameters().get(1).getValue());
                    }
                } else {
                    value = String.valueOf(booleanExpression(parameters.get(1)));
                }

                break;
            case "++":
            case "--":
                value = String.valueOf(expressionEvaluator.evaluateExpression(node.getParameters().get(1)));
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
