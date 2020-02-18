GRAAL ?= "graal-19.3"
GRAAL_HOME ?= "${GRAAL_SRC_HOME}/sdk/latest_graalvm_home"
GRAAL_SRC_HOME ?= "${HOME}/1/${GRAAL}/graal"
TRIMMED_CLIBRARIES_PATH ?= "/opt/${GRAAL}-clibraries"

download-all: download-libffi download-fdlibm

clean: clean-strictmath clean-fdlibm clean-libffi

clean-all:
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

# TODO platform specific CFLAGS
download-libffi: make-libffi-dirs
	cd target/archives; \
	curl -o libffi.tar.gz https://sourceware.org/pub/libffi/libffi-3.2.1.tar.gz

untar-libffi:
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

clean-libffi:
	rm -drf target/src/libffi/*
	rm -drf target/build/libffi/*
	rm -drf target/clibraries/libffi/*.a
	rm -drf target/clibraries/include/*

copy-graal-clibraries:
	rm -drf ${TRIMMED_CLIBRARIES_PATH}
	cp -r "${GRAAL_HOME}/lib/svm/clibraries" ${TRIMMED_CLIBRARIES_PATH}

link-openlibm: copy-graal-clibraries
	rm -f ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64/libstrictmath.a
	cp ${HOME}/1/openlibm/libopenlibm.3.0.dylib ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64
	cp -R ${HOME}/1/openlibm/libopenlibm.3.dylib ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64
	cp -R ${HOME}/1/openlibm/libopenlibm.dylib ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64

trim-graal-clibraries: copy-graal-clibraries
	rm -drf ${TRIMMED_CLIBRARIES_PATH}
	cp -r "${GRAAL_HOME}/lib/svm/clibraries" ${TRIMMED_CLIBRARIES_PATH}
	rm -f ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64/libffi.a
	rm -f ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64/include/ffi.h
	rm -f ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64/include/ffitarget.h
	rm -f ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64/include/svm_libffi.h
	rm -f ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64/include/trufflenfi.h
	rm -f ${TRIMMED_CLIBRARIES_PATH}/darwin-amd64/libstrictmath.a

make-fdlibm-dirs: make-dirs
	mkdir -p target/src/fdlibm || true
	mkdir -p target/clibraries/fdlibm || true

download-fdlibm: make-fdlibm-dirs
	cd target/src/fdlibm; \
	$(call get-fdlibm,makefile); \
	$(call get-fdlibm,fdlibm.h); \
	$(call get-fdlibm,e_acos.c); \
	$(call get-fdlibm,e_acosh.c); \
	$(call get-fdlibm,e_asin.c); \
	$(call get-fdlibm,e_atan2.c); \
	$(call get-fdlibm,e_atanh.c); \
	$(call get-fdlibm,e_cosh.c); \
	$(call get-fdlibm,e_exp.c); \
	$(call get-fdlibm,e_fmod.c); \
	$(call get-fdlibm,e_gamma.c); \
	$(call get-fdlibm,e_gamma_r.c); \
	$(call get-fdlibm,e_hypot.c); \
	$(call get-fdlibm,e_j0.c); \
	$(call get-fdlibm,e_j1.c); \
	$(call get-fdlibm,e_jn.c); \
	$(call get-fdlibm,e_lgamma.c); \
	$(call get-fdlibm,e_lgamma_r.c); \
	$(call get-fdlibm,e_log.c); \
	$(call get-fdlibm,e_log10.c); \
	$(call get-fdlibm,e_pow.c); \
	$(call get-fdlibm,e_rem_pio2.c); \
	$(call get-fdlibm,e_remainder.c); \
	$(call get-fdlibm,e_scalb.c); \
	$(call get-fdlibm,e_sinh.c); \
	$(call get-fdlibm,e_sqrt.c); \
	$(call get-fdlibm,k_cos.c); \
	$(call get-fdlibm,k_rem_pio2.c); \
	$(call get-fdlibm,k_sin.c); \
	$(call get-fdlibm,k_standard.c); \
	$(call get-fdlibm,k_tan.c); \
	$(call get-fdlibm,s_asinh.c); \
	$(call get-fdlibm,s_atan.c); \
	$(call get-fdlibm,s_cbrt.c); \
	$(call get-fdlibm,s_ceil.c); \
	$(call get-fdlibm,s_copysign.c); \
	$(call get-fdlibm,s_cos.c); \
	$(call get-fdlibm,s_erf.c); \
	$(call get-fdlibm,s_expm1.c); \
	$(call get-fdlibm,s_fabs.c); \
	$(call get-fdlibm,s_finite.c); \
	$(call get-fdlibm,s_floor.c); \
	$(call get-fdlibm,s_frexp.c); \
	$(call get-fdlibm,s_ilogb.c); \
	$(call get-fdlibm,s_isnan.c); \
	$(call get-fdlibm,s_ldexp.c); \
	$(call get-fdlibm,s_lib_version.c); \
	$(call get-fdlibm,s_log1p.c); \
	$(call get-fdlibm,s_logb.c); \
	$(call get-fdlibm,s_matherr.c); \
	$(call get-fdlibm,s_modf.c); \
	$(call get-fdlibm,s_nextafter.c); \
	$(call get-fdlibm,s_rint.c); \
	$(call get-fdlibm,s_scalbn.c); \
	$(call get-fdlibm,s_signgam.c); \
	$(call get-fdlibm,s_significand.c); \
	$(call get-fdlibm,s_sin.c); \
	$(call get-fdlibm,s_tan.c); \
	$(call get-fdlibm,s_tanh.c); \
	$(call get-fdlibm,w_acos.c); \
	$(call get-fdlibm,w_acosh.c); \
	$(call get-fdlibm,w_asin.c); \
	$(call get-fdlibm,w_atan2.c); \
	$(call get-fdlibm,w_atanh.c); \
	$(call get-fdlibm,w_cosh.c); \
	$(call get-fdlibm,w_exp.c); \
	$(call get-fdlibm,w_fmod.c); \
	$(call get-fdlibm,w_gamma.c); \
	$(call get-fdlibm,w_gamma_r.c); \
	$(call get-fdlibm,w_hypot.c); \
	$(call get-fdlibm,w_j0.c); \
	$(call get-fdlibm,w_j1.c); \
	$(call get-fdlibm,w_jn.c); \
	$(call get-fdlibm,w_lgamma.c); \
	$(call get-fdlibm,w_lgamma_r.c); \
	$(call get-fdlibm,w_log.c); \
	$(call get-fdlibm,w_log10.c); \
	$(call get-fdlibm,w_pow.c); \
	$(call get-fdlibm,w_remainder.c); \
	$(call get-fdlibm,w_scalb.c); \
	$(call get-fdlibm,w_sinh.c); \
	$(call get-fdlibm,w_sqrt.c)

define get-fdlibm
curl -o ${1} http://www.netlib.org/fdlibm/${1}
endef

# TODO platform specific CFLAGS
fdlibm:
	cd target/src/fdlibm; \
	CFLAGS="-fPIC -O1 -D_LITTLE_ENDIAN -ffunction-sections -fdata-sections -fvisibility=hidden" make; \
	cp libm.a ../../clibraries/fdlibm

clean-fdlibm:
	cd target/src/fdlibm; \
	make clean

make-strictmath-dirs:
	mkdir -p target/src/strictmath
	mkdir -p target/clibraries/strictmath

# TODO platform specific CFLAGS
strictmath: make-strictmath-dirs
	cd target/src/strictmath; \
	cp ${GRAAL_SRC_HOME}/substratevm/src/com.oracle.svm.native.strictmath/src/StrictMath.c .; \
	cp ${GRAAL_SRC_HOME}/substratevm/src/com.oracle.svm.native.strictmath/src/fdlibm/include/jfdlibm.h .; \
	cp ${GRAAL_SRC_HOME}/substratevm/src/com.oracle.svm.native.strictmath/src/fdlibm/include/fdlibm.h .; \
	gcc -MMD -fPIC -O1 -D_LITTLE_ENDIAN -ffunction-sections -fdata-sections -fvisibility=hidden -c StrictMath.c; \
	ar -rc ../../clibraries/strictmath/libstrictmath.a StrictMath.o

clean-strictmath:
	rm -drf target/src/strictmath/*