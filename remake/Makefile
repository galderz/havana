SHELL := bash
.ONESHELL:
.SHELLFLAGS := -eu -o pipefail -c
.DELETE_ON_ERROR:
MAKEFLAGS += --warn-undefined-variables
MAKEFLAGS += --no-builtin-rules

ifeq ($(origin .RECIPEPREFIX), undefined)
  $(error This Make does not support .RECIPEPREFIX. Please use GNU Make 4.0 or later)
endif
.RECIPEPREFIX = >

javafiles := $(wildcard *.java)
classfiles := $(patsubst %.java,%.class,$(javafiles))

run: main.jar
> java -jar $<

main.jar: $(classfiles)
> jar cvfm $@ Manifest.txt $^

%.class: %.java
> javac -d . -classpath . $<

compile: $(classfiles)

clean:
> rm -drf *.class
> rm -drf *.jar

.PHONY: compile run
