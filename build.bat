@echo off
rem Compile les sources Java et construit le fichier pont-mobile.jar
if exist out rmdir /s /q out
dir /s /b src\*.java > sources.txt
rem --release 11 : le .jar fonctionne avec Java 11 ou plus recent
javac -encoding UTF-8 --release 11 -d out @sources.txt
del sources.txt
jar --create --file pont-mobile.jar --main-class pontmobile.Main -C out .
echo Lancer la simulation avec : java -jar pont-mobile.jar
