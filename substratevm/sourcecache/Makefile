all: build native run

build:
	mvn clean source:jar install
	mvn dependency:copy-dependencies
	mvn dependency:copy-dependencies -Dclassifier=sources

native:
	native-image \
	-H:+ReportExceptionStackTraces \
	-H:+PrintAnalysisCallTree \
	-H:GenerateDebugInfo=1 \
	--no-fallback \
	--class-path sourcecache-client/target/dependency/slf4j-jboss-logging-1.2.0.Final.jar:sourcecache-client/target/dependency/slf4j-api-1.7.21.jar:sourcecache-client/target/dependency/jboss-logging-3.4.0.Final.jar:sourcecache-library/target/sourcecache-library-1.0-SNAPSHOT.jar \
	-jar sourcecache-client/target/sourcecache-client-1.0-SNAPSHOT.jar \
	sourcecache-client/target/sourcecache

run:
	./sourcecache-client/target/sourcecache andrew

native-attach:
	native-image \
	-H:+ReportExceptionStackTraces \
	-H:+PrintAnalysisCallTree \
	-H:GenerateDebugInfo=1 \
	--no-fallback \
	--debug-attach=*:8000 \
	--class-path sourcecache-client/target/dependency/slf4j-jboss-logging-1.2.0.Final.jar:sourcecache-client/target/dependency/slf4j-api-1.7.21.jar:sourcecache-client/target/dependency/jboss-logging-3.4.0.Final.jar:sourcecache-library/target/sourcecache-library-1.0-SNAPSHOT.jar \
	-jar sourcecache-client/target/sourcecache-client-1.0-SNAPSHOT.jar \
	sourcecache-client/target/sourcecache
