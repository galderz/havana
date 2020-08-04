package lang.system;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;

public class SystemLogging
{
    public static void main(String[] args)
    {
        final var logger = System.getLogger("This logger");
        logger.log(DEBUG, "This is a {0} message and does not show by default", DEBUG);
        logger.log(INFO, "This is a {0} message and shows", INFO);
        logger.log(WARNING, "This is an {0} and shows", WARNING);
        logger.log(ERROR, "This is an {0} and shows", ERROR);
    }
}
