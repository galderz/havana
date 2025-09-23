package havana;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JLine
{
    public static void main(String[] args) throws IOException
    {
        final Terminal terminal = TerminalBuilder
            .builder()
            .system(true)
            .build();

        final LineReader reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .build();

        // Background producer that won't disturb the current input line.
        final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "bg");
            t.setDaemon(true);
            return t;
        });

        exec.scheduleAtFixedRate(() -> {
            // IMPORTANT: always go through reader.printAbove(...)
            reader.printAbove("bg: message at " + Instant.now());
        }, 1, 2, TimeUnit.SECONDS);

        // Main input loop
        while (true) {
            String line;
            try {
                line = reader.readLine("> ");
            } catch (UserInterruptException | EndOfFileException e) {
                break;
            }

            if (line == null || line.equalsIgnoreCase("/quit")) {
                break;
            }

            // Echo user message without breaking the prompt:
            reader.printAbove("you: " + line);
        }

        exec.shutdownNow();
    }
}
