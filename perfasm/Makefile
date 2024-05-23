TOPDIR = ..
include $(TOPDIR)/make/common/MakeBase.gmk

default: run

BOOT_JDK_HOME ?= $(opt)/boot-java-21
CLASS ?= Test
CONF ?= release
JDK_HOME ?= $(HOME)/1/jdk21u-dev
include $(TOPDIR)/make/common/OpenJDK.gmk

jvm_args += -XX:+PrintNMethods
jvm_args += -XX:CompileOnly=Test.test

run: clear-logs $(java)
> $(call_java) \
>   $(jvm_args) \
>   $(shell pwd)/$(CLASS).java
.PHONY: run
