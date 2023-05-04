///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
//DEPS org.zeroturnaround:zt-exec:1.12

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.Callable;

@Command(name = "buffer", mixinStandardHelpOptions = true, version = "buffer 0.1",
    description = "buffer made with jbang")
class buffer implements Callable<Integer>
{
    private static final Random R = new Random();

    @Parameters(index = "0", description = "The greeting to print", defaultValue = "World!")
    private Path msgPath;

    public static void main(String... args)
    {
        int exitCode = new CommandLine(new buffer()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception
    {
//        final Path dotBuffer = dotBuffer();
//        final String msg = Files.readString(msgPath);
        System.out.println(nextDateTime());
        return 0;
    }

    static LocalDateTime nextDateTime()
    {
        final int nextMinute = R.nextInt(59);
        final LocalTime nextTime = LocalTime.of(13, nextMinute);
        final LocalDate nextDate = nextDate();
        return nextDate.atTime(nextTime);
    }

    static LocalDate nextDate()
    {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDate nextDate = todayOrTomorrow(now);
        return skipWeekend(nextDate);
    }

    private static LocalDate todayOrTomorrow(LocalDateTime now)
    {
        final LocalDate today = now.toLocalDate();
        return now.toLocalTime().getHour() > 13
            ? today.plusDays(1)
            : today;
    }

    private static LocalDate skipWeekend(LocalDate date)
    {
        return switch (date.getDayOfWeek())
        {
            case SATURDAY -> date.plusDays(2);
            case SUNDAY -> date.plusDays(1);
            default -> date;
        };
    }

    static Path dotBuffer()
    {
        final Path userHome = Path.of(System.getProperty("user.home"));
        final Path dotBuffer = userHome.resolve(Path.of(".buffer"));
        final File dotBufferFile = dotBuffer.toFile();
        if (!dotBufferFile.exists())
        {
            final boolean success = dotBufferFile.mkdir();
            if (!success)
                throw new RuntimeException("Unable to create ~/.buffer folder");
        }
        return dotBuffer;
    }
}
