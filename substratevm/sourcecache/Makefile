all: build native run

build:
	mvn clean source:jar package

native:
	native-image \
	-H:+ReportExceptionStackTraces \
	-H:GenerateDebugInfo=1 \
	--no-fallback \
	--class-path sourcecache-library/target/sourcecache-library-1.0-SNAPSHOT.jar \
	-jar sourcecache-client/target/sourcecache-client-1.0-SNAPSHOT.jar \
	sourcecache-client/target/sourcecache

run:
	./sourcecache-client/target/sourcecache andrew

native-attach:
	native-image \
	-H:+ReportExceptionStackTraces \
	-H:GenerateDebugInfo=1 \
	--no-fallback \
	--debug-attach=*:8000 \
	--class-path sourcecache-library/target/sourcecache-library-1.0-SNAPSHOT.jar \
	-jar sourcecache-client/target/sourcecache-client-1.0-SNAPSHOT.jar \
	sourcecache-client/target/sourcecache
