#!/bin/sh
# Compile les sources Java et construit le fichier pont-mobile.jar
rm -rf out
javac -encoding UTF-8 -d out $(find src -name "*.java")
jar --create --file pont-mobile.jar --main-class pontmobile.Main -C out .
echo "Lancer la simulation avec : java -jar pont-mobile.jar"
