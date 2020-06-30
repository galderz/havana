package svm.zipfs.graal;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

import java.util.Date;

@TargetClass(className = "jdk.nio.zipfs.ZipUtils")
final class Target_jdk_nio_zipfs_ZipUtils {

    // Use ZipUtils.overflowDosToJavaTime implementation to avoid consulting time zone offset
    @Substitute
    public static long dosToJavaTime(long dtime) {
        System.out.println("Use substituted dosToJavaTime() method");
        int year = (int) (((dtime >> 25) & 0x7f) + 1980);
        int month = (int) ((dtime >> 21) & 0x0f);
        int day = (int) ((dtime >> 16) & 0x1f);
        int hour = (int) ((dtime >> 11) & 0x1f);
        int minute = (int) ((dtime >> 5) & 0x3f);
        int second = (int) ((dtime << 1) & 0x3e);
        return new Date(year - 1900, month - 1, day, hour, minute, second).getTime();
    }

}
