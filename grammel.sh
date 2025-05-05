#!/bin/bash

build() {
    # Compile Java files
    javac -d out src/*.java
    
    # Create JAR files
    jar cfe out/grammel.jar Main -C out .
    jar cfe out/runtime.jar Runtime -C out .
    
    # Run Tokenizer
    echo "Running Tokenizer"
    echo ""
    java -jar out/grammel.jar $2      # <----- TEST FILE NAME GOES HERE

    # Run Parse Tree Generator
    echo "Running Parse Tree Generator"
    echo ""
    gprolog --consult-file src/grammel_parser.pl --consult-file out/parsed_query.pl
    echo ""
    echo "Build Successful"
}

# Function to run the runtime with the generated parse tree
run() {
    echo "Starting Runtime"
    java -jar out/runtime.jar out/prolog_out.pt $@
}


if [ "$1" = "run" ]; then
    shift
    run "$@"
elif [ "$1" = "build" ]; then
    build $@
fi