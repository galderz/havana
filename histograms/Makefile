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

jar := target/histograms-1.0-SNAPSHOT.jar
java = $(java_home)/bin/java
java_home := /opt/java-17
jvm_args += --add-exports=jdk.hotspot.agent/sun.jvm.hotspot.memory=ALL-UNNAMED
jvm_args += --add-exports=jdk.hotspot.agent/sun.jvm.hotspot.oops=ALL-UNNAMED
jvm_args += --add-exports=jdk.hotspot.agent/sun.jvm.hotspot.tools=ALL-UNNAMED
jvm_args += --add-exports=jdk.hotspot.agent/sun.jvm.hotspot.runtime=ALL-UNNAMED
jvm_args += --add-exports=jdk.hotspot.agent/sun.jvm.hotspot.gc.shared=ALL-UNNAMED
jvm_args += --add-exports=jdk.hotspot.agent/sun.jvm.hotspot.gc.parallel=ALL-UNNAMED
jvm_args += --add-modules
jvm_args += jdk.hotspot.agent
mvn += JAVA_HOME=$(java_home)
mvn += /opt/maven/bin/mvn

run: $(jar)
> $(java) $(jvm_args) -jar $<
.PHONY: run

$(jar):
> $(mvn) package

clean:
> $(mvn) clean
.PHONY: clean
