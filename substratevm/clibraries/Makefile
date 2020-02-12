GRAAL_SRC_HOME ?= "${HOME}/1/graal-19.3/graal"
GRAAL_HOME ?= "${GRAAL_SRC_HOME}/sdk/mxbuild/darwin-amd64/GRAALVM_LIBGRAAL_JAVA11_BNATIVE-IMAGE_BNATIVE-IMAGE-CONFIGURE_NI_NIC_NIL_NJU_SJVMCICOMPILER_SNATIVE-IMAGE-AGENT/graalvm-libgraal-java11-19.3.1/Contents/Home"
TRIMMED_CLIBRARIES_PATH ?= "/opt/graal-19.3-clibraries"

clean:
	rm -drf target

make-dirs:
	mkdir -p target/archives || true
	mkdir -p target/src || true
	mkdir -p target/build || true
	mkdir -p target/clibraries || true

make-libffi-dirs: make-dirs
	mkdir -p target/src/libffi || true
	mkdir -p target/build/libffi || true
	mkdir -p target/clibraries/libffi || true
	mkdir -p target/clibraries/libffi/include || true

download-libffi: make-libffi-dirs
	cd target/archives; \
	curl https://sourceware.org/pub/libffi/libffi-3.2.1.tar.gz > libffi.tar.gz

untar-libffi: download-libffi
	tar -xzvpf target/archives/libffi.tar.gz -C target/src/libffi --strip-components 1

configure-libffi: untar-libffi
	cd target/build/libffi; \
	../../src/libffi/configure \
	--disable-dependency-tracking \
	--disable-shared \
	--with-pic \
	CFLAGS="-g -O3"; \

libffi: configure-libffi
	cd target/build/libffi; \
	make; \
	cp .libs/libffi.a ../../clibraries/libffi; \
	cd include; \
	cp ffi.h ../../../clibraries/libffi/include; \
	cp `readlink ffitarget.h` ../../../clibraries/libffi/include

svm-libffi: libffi
	cp ${GRAAL_SRC_HOME}/substratevm/src/com.oracle.svm.libffi/include/svm_libffi.h target/clibraries/libffi/include
	cp ${GRAAL_SRC_HOME}/truffle/src/com.oracle.truffle.nfi.native/include/trufflenfi.h target/clibraries/libffi/include

trim-graal-clibraries:
	rm -drf ${TRIMMED_CLIBRARIES_PATH}
	cp -r "${GRAAL_HOME}/lib/svm/clibraries" ${TRIMMED_CLIBRARIES_PATH}
	rm -f ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64/libffi.a
	rm -f ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64/include/ffi.h
	rm -f ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64/include/ffitarget.h
	rm -f ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64/include/svm_libffi.h
	rm -f ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64/include/trufflenfi.h
