:begin
UNWIND [
{_id:0, properties:{name:'A'}}
, {_id:1, properties:{name:'B'}}
, {_id:2, properties:{name:'C'}}
] as row
CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row._id})
  SET n += row.properties SET n:Tree;
:commit

:begin
UNWIND [
{start: {_id:0}, end: {name:'B'}}
] as row
MATCH (start:`UNIQUE IMPORT LABEL`
               {`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:Tree {name: row.end.name})
CREATE (start)-[r:LEFT]->(end);
:commit

:begin
UNWIND [
{start: {_id:0}, end: {name:'C'}}
] as row
MATCH (start:`UNIQUE IMPORT LABEL`
               {`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:Tree {name: row.end.name})
CREATE (start)-[r:RIGHT]->(end);
:commit
