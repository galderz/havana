package org.example.seaofnodes;

import com.seaofnodes.simple.node.AddNode;
import com.seaofnodes.simple.node.ConstantNode;
import com.seaofnodes.simple.node.Node;
import com.seaofnodes.simple.type.TypeInteger;

public class Reassociation
{
    public static void main(String[] args)
    {
        final Node node = chain();
        System.out.println("Input: " + node);
        final Node reassociated = reassociate(node);
        System.out.println("Reassociated: " + reassociated);
    }

    static Node reassociate(Node root)
    {
        Node current = root;
        while (current.in(2) instanceof AddNode)
        {
            current = current.in(2);
        }
        final Node last = current.in(2);
        return rebuild(last, root);
    }

    static Node rebuild(Node node, Node root)
    {
        if (root.nIns() < 3)
        {
            return node;
        }
        return new AddNode(node, rebuild(root.in(1), root.in(2)));
    }

    static Node chain()
    {
        final AddNode n1 = new AddNode(
            new ConstantNode(new TypeInteger(2))
            , new ConstantNode(new TypeInteger(1))
        );
        final AddNode n2 = new AddNode(
            new ConstantNode(new TypeInteger(3))
            , n1
        );
        final AddNode n3 = new AddNode(
            new ConstantNode(new TypeInteger(4))
            , n2
        );
        final AddNode n4 = new AddNode(
            new ConstantNode(new TypeInteger(5))
            , n3
        );
        final AddNode n5 = new AddNode(
            new ConstantNode(new TypeInteger(6))
            , n4
        );
        final AddNode n6 = new AddNode(
            new ConstantNode(new TypeInteger(7))
            , n5
        );
        final AddNode n7 = new AddNode(
            new ConstantNode(new TypeInteger(8))
            , n6
        );
        return n7;
    }
}
