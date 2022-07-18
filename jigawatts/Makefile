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

REPO_BRANCH ?= master
REPO_FORK ?= rh-jmc-team
REPO ?= https://github.com/$(REPO_FORK)/$(repo_name)

mvn += JAVA_HOME=/opt/adopt-17
mvn += /opt/maven/bin/mvn
nss_path = /var/lib/sss/pipes/nss
nss_backup = /tmp/nss
pom = $(repo_path)/pom.xml
repo_name ?= jigawatts
repo_path = /tmp/$(repo_name)

test: $(pom) $(nss_backup)
> cd $(repo_path)
> sudo rm -drf src/test/resources
> sudo $(mvn) verify || true
> sudo mv $(nss_backup) $(nss_path)
.PHONY: test

$(nss_backup): $(nss_path)
> sudo mv $< $@

$(pom):
> cd /tmp
> git clone $(REPO)
> cd $(repo_name)
> git checkout $(REPO_BRANCH)

clean:
> cd $(repo_path)
> $(mvn) clean
.PHONY: clean

clean-all:
> sudo rm -drf $(repo_path)
.PHONY: clean-all
