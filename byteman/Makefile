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

BYTEMAN_HOME := /opt/byteman

jar := target/byteman.jar
btm_jar := $(BYTEMAN_HOME)/lib/byteman.jar

print = echo '$(1)'
comma := ,

script := $(comma)script:src/main/resources/assert.btm

sources += $(shell find src -type f -name '*.java')
sources += pom.xml

java += java
ifdef VERBOSE
  java += -Dorg.jboss.byteman.verbose=true
endif

run: $(jar)
> $(java) -javaagent:$(btm_jar)=boot:$(btm_jar)$(script) -jar $<

$(jar): $(sources)
> mvn package

clean:
> mvn clean
.PHONY: clean
