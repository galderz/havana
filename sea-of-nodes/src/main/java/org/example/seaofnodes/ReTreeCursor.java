package org.example.seaofnodes;

import com.seaofnodes.simple.node.AddNode;
import com.seaofnodes.simple.node.ConstantNode;
import com.seaofnodes.simple.node.Node;
import com.seaofnodes.simple.type.TypeInteger;

public class ReTreeCursor
{
    public static void main(String[] args)
    {
        Node node = chain8();
        System.out.println("Input: " + node);
        Node reassociated = reassociate(node, 8);
        System.out.println("Reassociated with a binary tree: " + reassociated);

        node = chain16();
        System.out.println("Input: " + node);
        reassociated = reassociate(node, 16);
        System.out.println("Reassociated with a binary tree: " + reassociated);
    }

    static Node reassociate(Node root, int depth)
    {
        Node current = root;
        while (current.in(2) instanceof AddNode)
        {
            current = current.in(2);
        }
        final Node last = current.in(2);
        final Node treeRoot = tree(depth, new Node[]{root}, "");
        return new AddNode(last, treeRoot);
    }

    static Node tree(int depth, Node[] cursor, String indent)
    {
        if (1 == depth)
        {
            final Node node = cursor[0];
            if (node instanceof AddNode)
            {
                cursor[0] = node.in(2);
                return node.in(1);
            }
            cursor[0] = null;
            return node;
        }

        final int leftDepth = depth / 2;
        final int rightDepth = depth - leftDepth;
        final Node left = tree(leftDepth, cursor, indent + " ");
        final Node right = tree(rightDepth, cursor, indent + " ");
        return new AddNode(left, right);
    }

    static Node chain8()
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

    static Node chain16()
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
        final AddNode n8 = new AddNode(
            new ConstantNode(new TypeInteger(9))
            , n7
        );
        final AddNode n9 = new AddNode(
            new ConstantNode(new TypeInteger(10))
            , n8
        );
        final AddNode n10 = new AddNode(
            new ConstantNode(new TypeInteger(11))
            , n9
        );
        final AddNode n11 = new AddNode(
            new ConstantNode(new TypeInteger(12))
            , n10
        );
        final AddNode n12 = new AddNode(
            new ConstantNode(new TypeInteger(13))
            , n11
        );
        final AddNode n13 = new AddNode(
            new ConstantNode(new TypeInteger(14))
            , n12
        );
        final AddNode n14 = new AddNode(
            new ConstantNode(new TypeInteger(15))
            , n13
        );
        final AddNode n15 = new AddNode(
            new ConstantNode(new TypeInteger(16))
            , n14
        );
        return n15;
    }
}
