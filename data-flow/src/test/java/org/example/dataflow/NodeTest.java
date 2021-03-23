package org.example.dataflow;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NodeTest
{
    @Test
    void postOrder()
    {
        //    A
        //   / \
        //  B   C
        //   \ /
        //    D

        final var d = new Node("d", List.of());
        final var b = new Node("b", List.of(d));
        final var c = new Node("c", List.of(d));
        final var a = new Node("a", List.of(b, c));

        var nodes = Nodes.postOrder(a)
            .stream()
            .map(Node::name)
            .collect(Collectors.toList());

        assertThat(nodes, is(List.of("d", "b", "c", "a")));
    }

//    @Test
//    void test000()
//    {
//        final var b4 = new Node("B4", List.of());
//        final var b3 = new Node("B3", List.of(b4));
//        final var b2 = new Node("B2", List.of(b3));
//        final var b7 = new Node("B7", List.of(b3));
//        final var b6 = new Node("B6", List.of(b7));
//        final var b8 = new Node("B8", List.of(b7));
//        final var b5 = new Node("B5", List.of(b6, b8));
//        final var b1 = new Node("B1", List.of(b2, b5));
//        final var b0 = new Node("B0", List.of(b1));
//
//        b3.call(b1); // cycle
//
//        final var root = b0;
//    }
}
