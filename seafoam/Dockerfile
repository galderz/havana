FROM fedora:36

RUN dnf install -y \
      graphviz \
      ruby  \
    && \
    gem install \
      seafoam

WORKDIR /data
