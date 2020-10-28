//     A
//    / \
//   B   C
//  / \   \
// D   E   F

:param rows => [{_id:0, properties:{name:'A'}}, {_id:1, properties:{name:'B'}}, {_id:2, properties:{name:'C'}}]

:begin
UNWIND $rows as row
CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row._id})
  SET n += row.properties SET n:Tree;
:commit

:param rows => [{_id:3, properties:{name:'D'}}, {_id:4, properties:{name:'E'}}, {_id:5, properties:{name:'F'}}]

:begin
UNWIND $rows as row
CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row._id})
  SET n += row.properties SET n:Tree;
:commit

:param rows => [{start: {_id:0}, end: {_id:1}}, {start: {_id:1}, end: {_id:3}}]

:begin
UNWIND $rows as row
MATCH (start:`UNIQUE IMPORT LABEL`
               {`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:`UNIQUE IMPORT LABEL`
               {`UNIQUE IMPORT ID`: row.end._id})
CREATE (start)-[r:LEFT]->(end);
:commit

:param rows => [{start: {_id:0}, end: {_id:2}}, {start: {_id:1}, end: {_id:4}}]

:begin
UNWIND $rows as row
MATCH (start:`UNIQUE IMPORT LABEL`
               {`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:`UNIQUE IMPORT LABEL`
               {`UNIQUE IMPORT ID`: row.end._id})
CREATE (start)-[r:RIGHT]->(end);
:commit

:param rows => [{start: {_id:2}, end: {_id:5}}]

:begin
UNWIND $rows as row
MATCH (start:`UNIQUE IMPORT LABEL`
               {`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:`UNIQUE IMPORT LABEL`
               {`UNIQUE IMPORT ID`: row.end._id})
CREATE (start)-[r:RIGHT]->(end);
:commit
