TOPDIR = ../../..
include $(TOPDIR)/make/common/MakeBase.gmk

BOOT_JDK_HOME ?= $(opt)/java-21
CONF_FAST ?= fast
CONF_RELEASE ?= release
CONF_SLOW ?= slow
JDK_HOME ?= $(HOME)/1/jdk

args_clone += -XX:CompileOnly=TestArrayClone.test
args_clone += $(current_dir)/$(test_clone)
args_copy += -XX:CompileOnly=TestArrayCopy.test
args_copy += $(current_dir)/$(test_copy)
capstone_home = $(opt)/capstone
capstone_makefile = $(capstone_home)/Makefile
capstone_lib = $(capstone_home)/libcapstone.a
conf_args += --with-boot-jdk=$(BOOT_JDK_HOME)
conf_args += --with-jmh=build/jmh/jars
current_dir = $(shell pwd)
dot_jmh = $(JDK_HOME)/build/jmh/jars/.jmh
java_fast = $(JDK_HOME)/build/$(CONF_FAST)/jdk/bin/java
java_release = $(JDK_HOME)/build/$(CONF_RELEASE)/jdk/bin/java
java_slow = $(JDK_HOME)/build/$(CONF_SLOW)/jdk/bin/java
jvm_args += -XX:-UseOnStackReplacement
jvm_args += -XX:-BackgroundCompilation
jvm_args += -XX:TieredStopAtLevel=1
jvm_args += -XX:+PrintIR
jvm_args += -XX:+PrintIRWithLIR
jvm_args += -XX:+PrintLIRWithAssembly
jvm_args += -XX:+Verbose
jvm_args += -Xlog:os
jvm_args += -Xlog:jit*=debug
usr_local := /usr/local
test_copy = TestArrayCopy.java
test_clone = TestArrayClone.java

run-clone: $(java_fast) $(test_clone) $(capstone_lib)
> LD_LIBRARY_PATH=$(capstone_home) $< $(jvm_args) $(args_clone)
.PHONY: run-clone

gdb-clone: $(java_slow) $(test_clone) $(capstone_lib)
> LD_LIBRARY_PATH=$(capstone_home) gdb --args $< $(jvm_args) $(args_clone)
.PHONY: gdb-clone

run-copy: $(java_fast) $(test_copy) $(capstone_lib)
> LD_LIBRARY_PATH=$(capstone_home) $< $(jvm_args) $(args_copy)
.PHONY: run-copy

gdb-copy: $(java_slow) $(test_copy) $(capstone_lib)
> LD_LIBRARY_PATH=$(capstone_home) gdb --args $< $(jvm_args) $(args_copy)
.PHONY: gdb-copy

$(java_fast): $(shell find $(JDK_HOME)/src -type f -name '*.java')
$(java_fast): $(shell find $(JDK_HOME)/test -type f -name '*.java')
$(java_fast):
> cd $(JDK_HOME)
> CONF=$(CONF_FAST) make

$(java_slow): $(shell find $(JDK_HOME)/src -type f -name '*.java')
$(java_slow): $(shell find $(JDK_HOME)/test -type f -name '*.java')
$(java_slow):
> cd $(JDK_HOME)
> CONF=$(CONF_slOW) make

$(java_release): $(shell find $(JDK_HOME)/src -type f -name '*.java')
$(java_release): $(shell find $(JDK_HOME)/test -type f -name '*.java')
$(java_release):
> cd $(JDK_HOME)
> CONF=$(CONF_RELEASE) make

config-fast: $(dot_jmh) $(capstone_lib)
> cd $(JDK_HOME)
> bash configure \
>   $(conf_args) \
>   --enable-hsdis-bundling \
>   --with-capstone=$(capstone_home) \
>   --with-conf-name=$(CONF_FAST) \
>   --with-debug-level=fastdebug \
>   --with-hsdis=capstone
.PHONY: config-fast

config-release: $(dot_jmh)
> cd $(JDK_HOME)
> bash configure \
>   $(conf_args) \
>   --with-conf-name=$(CONF_RELEASE)
.PHONY: config-release

config-slow: $(dot_jmh) $(capstone_lib)
> cd $(JDK_HOME)
> bash configure \
>   $(conf_args) \
>   --enable-hsdis-bundling \
>   --with-capstone=$(capstone_home) \
>   --with-conf-name=$(CONF_SLOW) \
>   --with-debug-level=slowdebug \
>   --with-hsdis=capstone
.PHONY: config-slow

reconfig-slow:
> cd $(JDK_HOME)
> CONF=slow make reconfigure
.PHONY: config-slow

$(dot_jmh):
> cd $(JDK_HOME)
> sh make/devkit/createJMHBundle.sh
> touch $@

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
