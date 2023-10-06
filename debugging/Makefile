COLIMA_CPU ?= 6
COLIMA_MEMORY ?= 16
COLIMA_VM ?= vz
TOPDIR = ..

default: run-tools-v2

include $(TOPDIR)/make/common/MakeBase.gmk
include $(TOPDIR)/make/common/Colima.gmk

boot_home = $(dir)/jdk-21+35
boot_java = $(boot_home)/bin/java
boot_tar = $(dir)/$(boot_tar_name)
boot_tar_name = adoptium
capstone_makefile = $(dir)/capstone/Makefile
dir := linux
jdk_makefile = $(dir)/jdk/Makefile

run-tools-v2: $(jdk_makefile) $(capstone_makefile) $(boot_java) copy-files build-tools-v2
> docker run -t -i --rm -v $(PWD)/linux:/opt jdk-tools:v2

build-tools-v2:
> docker build -f Dockerfile.v2 -t jdk-tools:v2 .
.PHONY: build-tools

run-tools-v1: build-tools-v1
> docker run -t -i --rm jdk-tools:v1

build-tools-v1:
> docker build -f Dockerfile.v1 -t jdk-tools:v1 .
.PHONY: build-tools

copy-files:
> cp make-capstone.sh linux
> cp make-jdk.sh linux

$(capstone_makefile):
> mkdir -p $(dir)
> cd $(dir)
> git clone https://github.com/capstone-engine/capstone

$(jdk_makefile):
> mkdir -p $(dir)
> cd $(dir)
> git clone https://github.com/openjdk/jdk

$(boot_java): $(boot_tar)
> pushd $(dir)
> tar -xzvpf $(boot_tar_name)
> popd
> touch $@

$(boot_tar):
> mkdir -p $(dir)
> cd $(dir)
> wget https://api.adoptium.net/v3/binary/latest/21/ea/linux/aarch64/jdk/hotspot/normal/adoptium

clean-boot:
> rm -drf $(boot_home)
> rm -drf $(boot_tar)
.PHONY: clean-boot
