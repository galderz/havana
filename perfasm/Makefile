TOPDIR = ..
include $(TOPDIR)/make/common/MakeBase.gmk

default: run

CONF ?= release
JDK_HOME ?= $(HOME)/1/jdk21u-dev
BOOT_JDK_HOME ?= $(opt)/boot-java-21
include $(TOPDIR)/make/common/OpenJDK.gmk

run: clear-logs $(java)
> $(call_java) --version
.PHONY: run
