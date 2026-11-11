@echo off
rem Compile les sources Java et construit le fichier pont-mobile.jar
if exist out rmdir /s /q out
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d out @sources.txt
del sources.txt
jar --create --file pont-mobile.jar --main-class pontmobile.Main -C out .
echo Lancer la simulation avec : java -jar pont-mobile.jar
