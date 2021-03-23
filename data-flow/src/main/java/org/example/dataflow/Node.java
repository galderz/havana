package org.example.dataflow;

import java.util.List;

public record Node(String name, List<Node> calls)
{
    void call(Node other)
    {
        calls.add(other);
    }
}
