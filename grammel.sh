javac -d out src/*.java
jar cfe out/grammel.jar org.kotlin.spring.Main -C out .
jar cfe out/runtime.jar org.kotlin.spring.Runtime -C out .
echo "Running Tokenizer"
echo ""
java -jar out/grammel.jar data/input.gl
echo "Running Parse Tree Generator"
echo ""


gprolog --consult-file src/grammel_parser.pl --consult-file out/parsed_query.pl
echo ""
echo "Loading the file to Runtime"
echo ""

java -jar out/runtime.jar out/prolog_out.pt
