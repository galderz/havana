all: clean build native run

clean:
	rm -f helloworld
	rm -f *.class

build:
	javac HelloWorld.java

native:
	native-image HelloWorld

run:
	./helloworld

call-tree:
	native-image -H:+PrintAnalysisCallTree HelloWorld
