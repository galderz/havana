javafiles := $(wildcard *.java)
classfiles := $(patsubst %.java,target/%.class,$(javafiles))

target/hello.jar: $(classfiles)
	jar cvfm target/hello.jar Manifest.txt $^

javasource: $(javafiles)

target/%.class: %.java
	@[ -d target ] || mkdir target
	javac -d target -classpath . $<

compile: $(classfiles)

clean:
	rm -drf target

.PHONY: javasource compile
