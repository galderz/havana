all: clean build native run

clean: clean-reports
	rm -f helloworld
	rm -f *.class

clean-reports:
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
