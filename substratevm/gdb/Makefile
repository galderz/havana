all: clean build native

clean:
	rm -f *.class
	rm -f hellogdb

build:
	javac -g HelloGdb.java

native:
	native-image -H:+TrackNodeSourcePosition HelloGdb
