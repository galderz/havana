clean:
	mvn clean
	rm -drf reports

build:
	mvn package

native:
	native-image -H:+PrintImageObjectTree --initialize-at-build-time=svm.recompute -jar target/recompute-1.0-SNAPSHOT.jar target/recompute
