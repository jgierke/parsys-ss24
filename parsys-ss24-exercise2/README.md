# ParSys SS24 Aufgabe 2

Um die Ausführung auch ohne IDE zu erleichtern, wurde das exec-maven-plugin eingebunden.
Damit die Outputs von Maven und der eigentlichen Anwendung leichter separiert werden können, schreibt das Programm auf stderr.
Unter Windows muss bei den beiden folgenden Befehlen stattdessen das Skript mvnw.cmd benutzt werden.

'''
mvnw compile
mvnw exec:java 2> output.txt
'''
