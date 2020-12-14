javafiles := $(wildcard *.java)
classfiles := $(patsubst %.java,%.class,$(javafiles))

run: main.jar
	java -jar $<

main.jar: $(classfiles)
	jar cvfm $@ Manifest.txt $^

%.class: %.java
	javac -d . -classpath . $<

compile: $(classfiles)

clean:
	rm -drf *.class
	rm -drf *.jar

.PHONY: compile run
