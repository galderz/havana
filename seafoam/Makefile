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

javafile := Fib.java
classfile := Fib.class

run: build
> docker run -t -i --rm \
>   -v $(PWD):/data \
>   -p 8080:8080 seafoam:v1 \
>   /bin/bash
.PHONY: run

build:
> docker build -t seafoam:v1 .
.PHONY: build

$(classfile): $(javafile)
> javac Fib.java
