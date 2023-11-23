TOPDIR = ../../..
include $(TOPDIR)/make/common/MakeBase.gmk

BOOT_JDK_HOME ?= $(opt)/java-21
CONF_FAST ?= fast
CONF_RELEASE ?= release
JDK_HOME ?= $(HOME)/1/jdk

capstone_home = $(opt)/capstone
capstone_makefile = $(capstone_home)/Makefile
capstone_lib = $(capstone_home)/libcapstone.a
capstone_usr_lib = $(usr_local)/lib/libcapstone.a
capstone_usr_os_lib = $(usr_local)/lib/libcapstone.5.dylib
capstone_os_lib = libcapstone.5.dylib
conf_args += --with-boot-jdk=$(BOOT_JDK_HOME)
conf_args += --with-jmh=build/jmh/jars
dot_jmh = $(JDK_HOME)/build/jmh/jars/.jmh
java_fast = $(JDK_HOME)/build/$(CONF_FAST)/jdk/bin/java
java_fast_makefile = $(JDK_HOME)/build/$(CONF_FAST)/Makefile
java_release = $(JDK_HOME)/build/$(CONF_RELEASE)/jdk/bin/java
jvm_args += -XX:-UseOnStackReplacement
jvm_args += -XX:-BackgroundCompilation
jvm_args += -XX:TieredStopAtLevel=1
jvm_args += -XX:+PrintIR
jvm_args += -XX:+PrintIRWithLIR
jvm_args += -XX:+PrintLIRWithAssembly
jvm_args += -XX:CompileOnly=Test.test
jvm_args += -XX:+Verbose
jvm_args += -Xlog:os
usr_local := /usr/local
test = Test.java

run: $(java_fast) $(test) $(capstone_os_lib)
> $< $(jvm_args) $(test)
.PHONY: run

$(java_fast): $(shell find $(JDK_HOME)/src -type f -name '*.java')
$(java_fast): $(shell find $(JDK_HOME)/test -type f -name '*.java')
$(java_fast):
> cd $(JDK_HOME)
> CONF=$(CONF_FAST) make

$(java_release): $(shell find $(JDK_HOME)/src -type f -name '*.java')
$(java_release): $(shell find $(JDK_HOME)/test -type f -name '*.java')
$(java_release):
> cd $(JDK_HOME)
> CONF=$(CONF_RELEASE) make

config-release: $(dot_jmh)
> cd $(JDK_HOME)
> bash configure \
>   $(conf_args) \
>   --with-conf-name=$(CONF_RELEASE)
.PHONY: config-release

config-fast: $(dot_jmh) $(capstone_usr_lib)
> cd $(JDK_HOME)
> bash configure \
>   $(conf_args) \
>   --enable-hsdis-bundling \
>   --with-capstone=$(usr_local) \
>   --with-conf-name=$(CONF_FAST) \
>   --with-debug-level=fastdebug \
>   --with-hsdis=capstone
.PHONY: config-fast

$(dot_jmh):
> cd $(JDK_HOME)
> sh make/devkit/createJMHBundle.sh
> touch $@

$(capstone_os_lib): $(capstone_usr_os_lib)
> ln -s $<

$(capstone_usr_lib): $(capstone_lib)
> cd $(capstone_home)
> sudo ./make.sh install

$(capstone_lib): $(capstone_makefile)
> cd $(capstone_home)
> ./make.sh

$(capstone_makefile):
> cd $(opt)
> git clone https://github.com/capstone-engine/capstone

commands:
> cd $(JDK_HOME)
> CONF=$(CONF_FAST) make compile-commands
.PHONY: commands

build:
> cd $(JDK_HOME)
> CONF=$(CONF_FAST) make
.PHONY: build

micro: $(java_release)
> cd $(JDK_HOME)
> CONF=$(CONF_RELEASE) make test TEST="micro:java.lang.ArrayClone" MICRO="JAVA_OPTIONS=-XX:TieredStopAtLevel=1"
.PHONY: micro

clean:
> cd $(JDK_HOME)
> CONF=$(CONF_FAST) make clean
.PHONY: clean
