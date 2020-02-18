all: clean build run native run-native

clean:
	rm -f daytwo
	rm -f *.class

build:
	javac DayTwo.java

run:
	java DayTwo

native:
	native-image DayTwo

run-native:
	./daytwo

call-tree:
	native-image -H:+PrintAnalysisCallTree DayTwo

jar:
	jar cvfm daytwo.jar Manifest.txt DayTwo.class

run-jar:
	java -jar daytwo.jar
