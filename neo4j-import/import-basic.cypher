:BEGIN

// clear data
MATCH (n:Company) DELETE n;

LOAD CSV WITH HEADERS FROM 'file:///import-basic/companies.csv' AS row
WITH row WHERE row.Id IS NOT NULL
MERGE (c:Company {companyId: row.Id, name: row.Name, location: row.Location});

:COMMIT