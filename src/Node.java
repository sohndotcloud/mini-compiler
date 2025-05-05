import java.util.List;

public class Node {
    private String name;
    private List<Node> parameters;
    private String value;

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getParameters() {
        return parameters;
    }

    public void setParameters(List<Node> parameters) {
        this.parameters = parameters;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Node{\n" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", " + parameters +
                '}';
    }
}
