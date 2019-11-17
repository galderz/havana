clean:
	rm HelloWorld.class || true

build:
	javac HelloWorld.java

image:
	docker build -t galderz/helloworld-java .

run:
	docker run -t galderz/helloworld-java
