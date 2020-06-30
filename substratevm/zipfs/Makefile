clean:
	rm -drf reports

build: clean
	mvn clean package

native: clean build
	native-image --initialize-at-build-time=svm.zipfs.SubstituteZipFsAtBuildTime,svm.zipfs.graal -jar target/zipfs-1.0-SNAPSHOT.jar target/zipfs
