import java.util.HashMap;
import java.util.List;

/**
 * Evaluator for parse tree expressions that produces string expressions
 * and evaluates to integers.
 */
public class ExpressionEvaluator {
    private HashMap<String, Variable> variables;
    private List<String> operators = List.of("++", "--");
    /**
     * Constructor that accepts an existing variable HashMap
     */
    public ExpressionEvaluator(HashMap<String, Variable> variables) {
        this.variables = variables;
    }

    /**
     * Convert a parse tree node to a string expression
     */
    public String expressionToString(Node node) {
        if (node == null) {
            return "";
        }

        String nodeName = node.getName();
        List<Node> parameters = node.getParameters();
        String value = node.getValue();

        // If node has a direct value, return it
        if (value != null && (parameters == null || parameters.isEmpty())) {
            return value;
        }

        // Handle leaf nodes
        if (parameters == null || parameters.isEmpty()) {
            if (nodeName.equals("number")) {
                return value;
            } else if (nodeName.equals("name")) {
                return value;
            } else {
                return value != null ? value : nodeName;
            }
        }

        // Handle different node types with parameters
        StringBuilder result = new StringBuilder();

        switch (nodeName) {
            case "add":
                if (parameters.size() >= 2) {
                    String left = expressionToString(parameters.get(0));
                    String right = expressionToString(parameters.get(1));
                    result.append(left).append(" + ").append(right);
                }
                break;

            case "subt":
                if (parameters.size() >= 2) {
                    String left = expressionToString(parameters.get(0));
                    String right = expressionToString(parameters.get(1));
                    result.append(left).append(" - ").append(right);
                }
                break;

            case "mult":
                if (parameters.size() >= 2) {
                    String left = expressionToString(parameters.get(0));
                    String right = expressionToString(parameters.get(1));
                    result.append(left).append(" * ").append(right);
                }
                break;

            case "divi":
                if (parameters.size() >= 2) {
                    String left = expressionToString(parameters.get(0));
                    String right = expressionToString(parameters.get(1));
                    result.append(left).append(" / ").append(right);
                }
                break;

            case "paran":
                if (!parameters.isEmpty()) {
                    String expr = expressionToString(parameters.get(0));
                    result.append("(").append(expr).append(")");
                }
                break;

            default:
                // For function calls or unrecognized node types
                result.append(nodeName).append("(");
                for (int i = 0; i < parameters.size(); i++) {
                    if (i > 0) {
                        result.append(", ");
                    }
                    result.append(expressionToString(parameters.get(i)));
                }
                result.append(")");
                break;
        }

        return result.toString();
    }

    /**
     * Evaluate an expression to an integer value
     */
    public double evaluateExpression(Node node) {
        if (node == null) {
            return 0;
        }

        String nodeName = node.getName();
        List<Node> parameters = node.getParameters();
        String value = node.getValue();

        if (parameters == null || parameters.isEmpty()) {
            if (nodeName.equals("number") || (value != null && isNumeric(value))) {
                return Integer.parseInt(value);
            } else if (nodeName.equals("value")) {
                String varName = value;
                if (variables.containsKey(varName)) {
                    return Integer.parseInt(variables.get(varName).getValue());
                } else {
                    throw new RuntimeException("Undefined variable: " + varName);
                }
            } else if (nodeName.equals("valueOp")) {
                String operator = value;
                if (operator.equals("++") || operator.equals("--")) {
                    return 0;
                }
            }
            return 0;
        }

        // Handle operations
        switch (nodeName) {
            case "add":
                if (parameters.size() >= 2) {
                    double result = evaluateExpression(parameters.get(0)) + evaluateExpression(parameters.get(1));
                    return result;                }
                break;

            case "subt":
                if (parameters.size() >= 2) {
                    double result = evaluateExpression(parameters.get(0)) - evaluateExpression(parameters.get(1));
                    return result;                }
                break;

            case "mult":
                if (parameters.size() >= 2) {
                    double result = evaluateExpression(parameters.get(0)) * evaluateExpression(parameters.get(1));
                    return result;
                }
                break;

            case "divi":
                if (parameters.size() >= 2) {
                    double result = evaluateExpression(parameters.get(0)) / evaluateExpression(parameters.get(1));
                    return result;                }
                break;

            case "paran":
                double result = evaluateExpression(parameters.get(0));
                return result;
            case "assign":
                if (parameters.size() >= 2) {
                    Node firstParam = parameters.get(0);
                    Node secondParam = parameters.get(1);
                    int delta = 0;
                    Variable variable;
                    if (operators.contains(firstParam.getValue())) {
                        if ("++".equals(secondParam.getValue())) {
                            delta = 1;
                        } else if ("--".equals(secondParam.getValue())) {
                            delta = -1;
                        }
                        variable = variables.get(firstParam.getValue());
                        variable.setValue(String.valueOf(Integer.parseInt(variable.getValue()) + delta));

                    } else if (operators.contains(secondParam.getValue())) {
                        if ("++".equals(secondParam.getValue())) {
                            delta = 1;
                        } else if ("--".equals(secondParam.getValue())) {
                            delta = -1;
                        }
                        variable = variables.get(firstParam.getValue());
                        variable.setValue(String.valueOf(Integer.parseInt(variable.getValue()) + delta));
                        return Integer.parseInt(variable.getValue());
                    }
                }
                break;
        }

        return 0;
    }

    /**
     * Check if a string is a valid integer
     */
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (i == 0 && str.charAt(i) == '-') {
                if (str.length() == 1) {
                    return false;
                }
                continue;
            }
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }


    public int handleIncrementDecrement(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }

        String nodeName = node.getName();
        List<Node> parameters = node.getParameters();

        boolean isPrefix = false;
        boolean isIncrement = false;
        String variableName = null;

        if ("assign".equals(nodeName) && parameters != null && parameters.size() >= 2) {
            Node firstParam = parameters.get(0);
            Node secondParam = parameters.get(1);

            if ("name".equals(firstParam.getName()) &&
                    (secondParam.getValue().equals("++") || secondParam.getValue().equals("--"))) {
                isPrefix = false;
                isIncrement = "++".equals(secondParam.getValue());
                variableName = firstParam.getValue();
            } else if ((firstParam.getValue().equals("++") || firstParam.getValue().equals("--")) &&
                    "name".equals(secondParam.getName())) {
                isPrefix = true;
                isIncrement = "++".equals(firstParam.getValue());
                variableName = secondParam.getValue();
            }
        }

        if (variableName == null) {
            throw new IllegalArgumentException("Invalid increment/decrement operation");
        }

        if (!variables.containsKey(variableName)) {
            throw new RuntimeException("Undefined variable: " + variableName);
        }

        Variable variable = variables.get(variableName);
        int currentValue = Integer.parseInt(variable.getValue());
        int newValue = isIncrement ? currentValue + 1 : currentValue - 1;

        variable.setValue(String.valueOf(newValue));

        return isPrefix ? newValue : currentValue;
    }
}