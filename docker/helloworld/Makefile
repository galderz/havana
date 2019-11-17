clean:
	rm HelloWorld.class || true

build:
	javac HelloWorld.java

image:
	docker build -t galderz/helloworld-java .

# Order of image and env variable name matter!
# --env goes BEFORE image name
# It won't be applied if env is defined after
run:
	docker run -t --env SITE_NAME=A galderz/helloworld-java
