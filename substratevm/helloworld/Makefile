all: clean build native run

clean:
	rm -f helloworld
	rm -f *.class
	rm -drf reports

build:
	javac HelloWorld.java

native:
	native-image HelloWorld

run:
	./helloworld

call-tree:
	native-image -H:+PrintAnalysisCallTree HelloWorld

jar:
	jar cvfm helloworld.jar Manifest.txt HelloWorld.class

run-jar:
	java -jar helloworld.jar

method-histogram:
	native-image -H:+PrintMethodHistogram HelloWorld
