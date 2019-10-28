clean:
	mvn clean

build:
	mvn package

native:
	native-image \
	-jar target/rsa-0.1.jar \
	-H:+TraceSecurityServices \
	-H:+PrintClassInitialization \
	--initialize-at-build-time=svm.rsa.KeyFactoryRSA \
	target/keyfactoryrsa

native-with-security:
	native-image \
	--enable-all-security-services \
	-H:+TraceSecurityServices \
	-jar target/rsa-0.1.jar \
	target/keyfactoryrsa
