#!/usr/bin/env bash

echo "Starting The Compilation Process"|lolcat
filename=$1
compile (){
    echo "compiling $filename";
    java -jar Octoc/octoc.jar $filename
    cd Octoc/
    javac -classpath src/ src/compiler/octo.java
    cd src
    java compiler.octo
}
compile $1|lolcat
