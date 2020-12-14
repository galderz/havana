javafiles := $(wildcard *.java)
targetclassfiles := $(patsubst %.java,target/%.class,$(javafiles))
classfiles := $(patsubst %.java,%.class,$(javafiles))

target/hello.jar: $(targetclassfiles)
	cd target \
		&& jar cvfm hello.jar ../Manifest.txt $(classfiles)

target/%.class: %.java
	@[ -d target ] || mkdir target
	javac -d target -classpath . $<

compile: $(targetclassfiles)

clean:
	rm -drf target

.PHONY: compile
