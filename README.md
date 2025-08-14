## This mini-compiler is an AST generator with a compiler that runs the program.

## Features
- *Primitive Types*: int, string, boolen
- *Operators*: <,>,==,<=,>=, +=, -=
- *Assignment Operator*: x++, --y
- *Conditional Constructor*
- *Ternary Expressions*:	cond ? a : b for values and booleans
- *Boolean Ternary Nesting*:	Multiple ? : expressions inside conditions
- *Loops*: for, while, do-while
- *Custom Block Scoping*:	Uses {} in DCG via BlockScope
- *Print*: Statement for Output

## System Support
- MacOS, Windows and Linux

## Tools Used
- *Paser*: DCG in Prolog
- *Lexer*: Handwritten in Prolog using pattern-matching
- *Interpreter/Runtime*: Implemented in Prolog
- *Version Control*: GitHub (private repository)
- *Build Tool*: Build

## Project Structure

```
Grammel/
├── parser.pl              % Prolog grammar file using DCG rules
├── test_paser.pl          % Sample token stream and parse test
├── src/
│   ├── Main.java          % Entry point to run the tokenizer
│   ├── grammel_lexer.java
│   ├── Token.java
│   ├── TokenType.java
├── README.md
```

## Requirements

- **gprolog** installed
- **Java 8-14** required for evaluating expressions using a javascript engine

## ⚙️ Build Instructions

### 🧪 Run the Tokenizer

```bash
cd Grammel
javac -d out src/*.java
java -cp out Main
```

- This will print a list of tokens, e.g.:
```
[int, x, =, 5, ;, print, x, ;]
```

### Run the Parser

```
./grammel.sh
```


## Sample Program (in Grammel Syntax)

```c
int methodName(int x, int y) {
    int z = 234;
    z = 27;

    if (x == y) {
        y = 100;
    else if {
        z = 10;
    } else {
        x = 100;
    }

    while (x < 3) {
        z = x == y ? true : false;
        x++;
    }
}
```


Use this in SWI-Prolog to test your parser.

## Team Members
- =Nils Sohn
