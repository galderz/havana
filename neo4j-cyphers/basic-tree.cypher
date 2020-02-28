:BEGIN

CREATE (t:TreeNode {name: 'A'});
CREATE (t:TreeNode {name: 'B'});
CREATE (t:TreeNode {name: 'C'});

MATCH (a:TreeNode),(b:TreeNode)
  WHERE a.name = 'A' AND b.name = 'B'
CREATE (a)-[r:LEFT]->(b);

MATCH (a:TreeNode),(c:TreeNode)
  WHERE a.name = 'A' AND c.name = 'C'
CREATE (a)-[r:RIGHT]->(c);

:COMMIT
