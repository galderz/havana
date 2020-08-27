package file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class BackupOrRestore
{
    public static void main(String[] args)
    {
        try
        {
            final var base = System.getProperty("base");
            Path backupMxPy = Path.of(base, "mx.py.backup");
            Path mxPy = Path.of(base, "mx.py");
            if (!backupMxPy.toFile().exists())
            {
                Files.copy(mxPy, backupMxPy);
            }
            else
            {
                Files.copy(backupMxPy, mxPy, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
