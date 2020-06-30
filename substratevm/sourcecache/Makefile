all: build native run

build:
	mvn clean package

native: build
	native-image \
	-H:+ReportExceptionStackTraces \
	--no-fallback \
	--class-path sourcecache-library/target/sourcecache-library-1.0-SNAPSHOT.jar \
	-jar sourcecache-client/target/sourcecache-client-1.0-SNAPSHOT.jar \
	sourcecache-client/target/sourcecache

run:
	./sourcecache-client/target/sourcecache andrew
