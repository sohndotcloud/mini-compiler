import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Please provide a file path as the first argument.");
            return;
        }

        String filePath = args[0];
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file:");
            e.printStackTrace();
        }

        GrammelLexer tokenizer = new GrammelLexer(sb.toString());
        List<Token> tokens = tokenizer.tokenize();

        System.out.println("Prolog-Token List Generating\n");
        StringBuilder tokenParams = new StringBuilder();
        StringBuilder programQuery = new StringBuilder();
        tokenParams.append("[");
        for (int i = 0; i < tokens.size(); i++) {
            tokenParams.append(tokens.get(i).lexeme);
            if (i < tokens.size() - 2) {
                tokenParams.append(", ");
            }
        }
        tokenParams.append("]");

        programQuery.append(":- initialization(run_query).\n\n");
        programQuery.append("run_query :-\n");
        programQuery.append("tell('out/prolog_out.pt'),\n");
        programQuery.append("program(ParseTree, ");
        programQuery.append(tokenParams);
        programQuery.append(", []),\n");
        programQuery.append("write(ParseTree), nl, told, halt.\n");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("out/parsed_query.pl", false);
            fileOutputStream.write(programQuery.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
