package org.example.dataflow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Nodes
{
    static List<Node> postOrder(Node node)
    {
        var result = new ArrayList<Node>();
        postOrder(node, result, new HashSet<>());
        return result;
    }

    private static void postOrder(Node node, List<Node> result, Set<Node> visited)
    {
        for (Node called : node.calls())
        {
            postOrder(called, result, visited);
        }

        if (!visited.contains(node))
        {
            result.add(node);
            visited.add(node);
        }
    }
}
