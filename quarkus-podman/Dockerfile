FROM fedora:32

ADD "https://code.quarkus.io/d?g=org.acme&a=code-with-quarkus&v=1.0.0-SNAPSHOT&b=MAVEN&c=org.acme.ExampleResource&s=&cn=code.quarkus.io" quarkus.zip

RUN dnf -y install \
        java-11-openjdk-devel \
        podman \
        unzip

RUN unzip quarkus.zip

RUN export JAVA_HOME=$(dirname $(dirname $(readlink $(readlink $(type -p javac)))))

# cd code-with-quarkus && ./mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=podman
