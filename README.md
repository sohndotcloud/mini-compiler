# SER-502-Grammel-Team24
## Overview:-
Everyone has their favorite programming language and their little gripes about the language. This project is your chance to try and do better! You are to design, implement, and demonstrate your own language. You are to implement a lexical analyzer, parser, and runtime environment for a simple programming language. This is a team project and is expected to be completed in teams of four students.

## About the Language
Grammel: is a simple and powerful programming language designed to feel familiar to anyone who has used *Java* or *Python*. It combines the strong typing and structured style of Java with the clean, easy-to-read syntax of Python. This makes it a great choice for writing programs that are both clear and reliable.
You can use Grammel to write loops, conditional statements, and print output—without any unnecessary complexity. Everything is designed to be easy to understand and use.
What makes Grammel unique is how it's built. It uses something called *Definite Clause Grammars (DCG)* to define its syntax and behavior. This approach not only makes the language work smoothly, but also makes it a great example of how programming languages are built and executed. That’s why Grammel is perfect for students, beginners, or anyone who wants to learn more about how languages work behind the scenes.

## Features (As per Milestone 2)
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
- **Java 11** required for boolean expressions

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
