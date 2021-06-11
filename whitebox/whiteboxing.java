///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA_OPTIONS -Xbootclasspath/a:/opt/java-11/wb.jar -XX:+UnlockDiagnosticVMOptions -XX:+WhiteBoxAPI

import static java.lang.System.*;

import sun.hotspot.WhiteBox;

// TODO try out examples in https://jpbempel.github.io/2015/07/07/whitebox-api.html
public class whiteboxing {

    public static void main(String... args) {
        System.out.printf("args at: %x\n",
            WhiteBox.getWhiteBox().getObjectAddress(args));
    }
}
