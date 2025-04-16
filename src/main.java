import java.util.List;

public class Main {
    public static void main(String[] args) {
        String code = """
            int n(int x, int y) {
                int z = 234;
                z = 22;

                if (x == y) {
                    y = 5;
                } else if (true) {
                    x = 5;
                } else {
                    x = 10;
                }

                while (x > y) {
                    z = x == y ? true : false;
                }
            }
        """;

        GrammelTokenizer tokenizer = new GrammelTokenizer(code);
        List<GrammelToken> tokens = tokenizer.tokenize();

        System.out.println("Prolog-Compatible Token List:");
        System.out.print("[");
        for (int i = 0; i < tokens.size(); i++) {
            System.out.print(tokens.get(i));
            if (i < tokens.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}
