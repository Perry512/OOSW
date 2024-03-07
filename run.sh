#!/bin/bash

# Compile all Java files and put the compiled classes in the 'classes' directory
javac -d classes -classpath "classes:." model/*.java common/*.java database/*.java event/*.java exception/*.java impresario/*.java Utilities/*.java

# Compile the TestAssgn1.java file
javac -classpath "classes:." TestAssgn1.java

# Run the TestAssgn1 class with the required classpath and dependencies
java -cp "mariadb-java-client-3.0.3.jar:classes:." TestAssgn1
