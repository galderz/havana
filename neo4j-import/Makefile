.PHONY: import-basic

NEO_HOME ?= /opt/neo4j

neobin = $(NEO_HOME)/bin
neodata = $(NEO_HOME)/data
neoimport = $(NEO_HOME)/import

import-basic:
	cp -r import-basic $(neoimport)/import-basic
	cat import-basic.cypher | $(neobin)/cypher-shell -u $(NEO_USER) -p $(NEO_PASS)

clean: clean-neo4j

clean-neo4j:
	echo "MATCH (n) DETACH DELETE n;" | $(neobin)/cypher-shell -u $(NEO_USER) -p $(NEO_PASS)

neo-start:
	${neobin}/neo4j console

# See https://community.neo4j.com/t/mismatching-store-id-on-neo4j-admin-import/20504/4 for required steps:
#   Do set `dbms.recovery.fail_on_missing_files=false` in conf/neo4j.conf
#   Then the clean can work
# Note that `neo4j-admin` should be used for big datasets (+10m records)
#   see https://neo4j.com/developer/guide-import-csv/ for more info
neo-admin-import: neo-admin-clean
	${neobin}/neo4j-admin import \
	--nodes=basic/movies.csv \
	--nodes=basic/actors.csv \
	--relationships=basic/roles.csv

neo-admin-clean:
	rm -rf $(neodata)/databases/neo4j/*
	rm -rf $(neodata)/transactions/*
