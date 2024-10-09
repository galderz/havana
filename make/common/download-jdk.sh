#!/bin/bash
set -eu

VERSION=$1
DIR=$2

pushd ${DIR}

if [ "$(uname)" = "Darwin" ]; then
  OS="mac"
  ARCH="aarch64"
else
  OS=$(uname | tr A-Z a-z)
  ARCH="x64"
fi

# Specify the Java version and platform
API_URL="https://api.adoptium.net/v3/binary/latest/${VERSION}/ga/${OS}/${ARCH}/jdk/hotspot/normal/eclipse"

# Fetch the archive
FETCH_URL=$(curl -s -w %{redirect_url} "${API_URL}")
FILENAME=$(curl -OLs -w %{filename_effective} "${FETCH_URL}")

# Validate the checksum
curl -Ls "${FETCH_URL}.sha256.txt" | sha256sum -c --status

echo "Downloaded successfully as ${FILENAME}"

mkdir -p temurin-${VERSION}
pushd temurin-${VERSION}
tar --strip-components 1 -xzvpf ../${FILENAME}
popd

if [ "$(uname)" = "Darwin" ]; then
  ln -s temurin-${VERSION}/Contents/Home java-${VERSION}
else
  ln -s temurin-${VERSION} java-${VERSION}
fi

popd
