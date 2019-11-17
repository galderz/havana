FROM openjdk:11
ENV CLASSES=/app/classes
ADD HelloWorld.class $CLASSES/
WORKDIR $CLASSES
ENTRYPOINT ["java", "HelloWorld"]
