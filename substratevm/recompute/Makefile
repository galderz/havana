clean:
	rm -drf reports

build: clean
	mvn clean package

native: clean build
	native-image -H:+PrintImageObjectTree --initialize-at-build-time=svm.recompute -jar target/recompute-1.0-SNAPSHOT.jar target/recompute
