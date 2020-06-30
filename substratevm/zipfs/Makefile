clean:
	rm -drf reports

build: clean
	mvn clean package

native: clean build
	native-image -jar target/zipfs-1.0-SNAPSHOT.jar target/zipfs
