package kube;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Function;

public class Kubernetes
{
    Queue<Op> operations = new ArrayDeque<>();

    public Function<String, Boolean> createNamespace()
    {
        return namespaceName ->
        {
            final Op top = operations.peek();
            if (isError(top))
            {
                operations.remove();
                return false;
            }
            operations.add(new Op(namespaceName, OpType.CREATE_NAMESPACE));
            return true;
        };
    }

    public Function<String, Boolean> existsNamespace()
    {
        return namespaceName ->
        {
            if (isNamespaceCreated())
            {
                return true;
            }

            return false;
        };
    }

    private boolean isNamespaceCreated()
    {
        // TODO check if retrived
        return operations.stream().anyMatch(op -> op.type == OpType.CREATE_NAMESPACE);
    }

//    public static Consumer<Kubernetes> apply()
//    {
//        return null;
//    }

    private final static class Op
    {
        Object param;
        OpType type;

        public Op(Object param, OpType type)
        {
            this.param = param;
            this.type = type;
        }
    }

    private enum OpType
    {
        CREATE_NAMESPACE,
        GET_NAMESPACE,
        ERROR,
    }

    private static boolean isError(Op op)
    {
        return op != null && op.type == OpType.ERROR;
    }
}
