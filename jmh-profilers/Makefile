TOPDIR = ..
include $(TOPDIR)/make/common/MakeBase.gmk

default: prof

BOOT_JDK_HOME ?= $(opt)/boot-java-21
CLASS ?= Test
CONF ?= release
JDK_HOME ?= $(HOME)/1/jdk21u-dev
include $(TOPDIR)/make/common/OpenJDK.gmk

jvm_args += -XX:+UnlockDiagnosticVMOptions
jvm_args += -XX:+PrintNMethods
jvm_args += -XX:CompileOnly=Test.test

PROF ?= perf
TIME ?= 10

run: clear-logs $(java)
> $(call_java) \
>   $(jvm_args) \
>   $(shell pwd)/$(CLASS).java
.PHONY: run

APP_DIR = $(HOME)/1/jmh
JAVA_HOME ?= $(JDK_HOME)/build/$(conf_name)/jdk
benchmarks_jar = $(APP_DIR)/jmh-samples/target/benchmarks.jar
include $(TOPDIR)/make/common/Maven.gmk

prof: $(benchmarks_jar) $(java)
> $(java) -jar $(benchmarks_jar) JMHSample_01 -f 1 -i 1 -r $(TIME) -wi 1 -w $(TIME) -prof $(PROF)
.PHONY: perfasm

$(benchmarks_jar): $(shell find $(JMH_HOME) -type f -name '*.java')
$(benchmarks_jar): $(java) $(mvnw)
> cd $(APP_DIR)
> $(mvn) clean install -DskipTests
