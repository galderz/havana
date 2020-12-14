javafiles := $(wildcard *.java)

target/hello.jar: $(classfiles)
	jar cvfm target/hello.jar Manifest.txt $^

javasource: $(javafiles)

target/%.class: %.java
	@[ -d target ] || mkdir target
	javac -d target -classpath . $<

classfiles := $(patsubst %.java,target/%.class,$(javafiles))
compile: $(classfiles)

.PHONY: javasource compile
