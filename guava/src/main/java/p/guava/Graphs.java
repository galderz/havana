package p.guava;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.Objects;

public class Graphs
{
    public static void main(String[] args)
    {
        final var graph = GraphBuilder.directed().build();
        graph.putEdge("A", "B");
        System.out.println(graph);

        MutableValueGraph<GraphNode, Double> weightedGraph = ValueGraphBuilder.directed().build();
        GraphNode a = new GraphNode("Jonathan", 20);
        GraphNode b = new GraphNode("Nicolas", 40);
        GraphNode c = new GraphNode("Georgia", 30);
        weightedGraph.putEdgeValue(a, b, 2.0);
        weightedGraph.putEdgeValue(a, c, 4.5);
        System.out.println(weightedGraph);
    }

    public static final class GraphNode {
        private final String name;
        private final int age;

        GraphNode(String name, int age) {
            this.name = Objects.requireNonNull(name, "name");
            this.age = age;
        }

        public String name() {
            return name;
        }

        public int age() {
            return age;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof GraphNode) {
                GraphNode that = (GraphNode) other;
                return this.name.equals(that.name)
                    && this.age == that.age;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }

        @Override
        public String toString() {
            return "(" + name + ", " + age + ")";
        }
    }

}
