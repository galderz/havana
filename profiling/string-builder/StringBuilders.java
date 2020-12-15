/**
 * This demo shows that most sampling profilers are misleading.
 * The given program appends 5 symbols to the end of StringBuilder and deletes 5 symbols from the beginning of StringBuilder.
 * <p>
 * The real bottleneck here is delete(),
 * since it involves moving of 1 million characters.
 * However, safepoint-based profilers will display Thread.isAlive() as the hottest method.
 * JFR will not port anything useful at all,
 * since it cannot traverse stack traces when JVM is running System.arraycopy().
 *
 * @see <a href="https://github.com/apangin/codeone2019-java-profiling/blob/master/src/demo1/StringBuilderTest.java">
 *     Based on Andrei Pangin's example
 *     </a>
 */
public class StringBuilders
{
    public static void main(String[] args)
    {
        System.out.println("Run...");
        StringBuilder sb = new StringBuilder();
        sb.append(new char[1_000_000]);

        do
        {
            sb.append(12345);
            sb.delete(0, 5);
        } while (Thread.currentThread().isAlive());

        System.out.println(sb);  // never happens
    }
}
