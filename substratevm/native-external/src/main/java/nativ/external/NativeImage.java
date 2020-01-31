package nativ.external;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NativeImage
{
    public static void main(String[] args) throws Exception
    {
        final Stream<String> xxPlus = Stream.of(
            "UnlockExperimentalVMOptions"
            , "EnableJVMCI"
        ).map(JavaOptions::xxPlus);

        final Stream<String> systemProperties = Stream.of(new String[][]{
            {"truffle.TrustAllTruffleRuntimeProviders", "true"}
            , {"truffle.TruffleRuntime", "com.oracle.truffle.api.impl.DefaultTruffleRuntime"}
            , {"graalvm.ForcePolyglotInvalid", "true"}
            , {"graalvm.locatorDisabled", "true"}
            , {"substratevm.IgnoreGraalVersionCheck", "true"}
            , {"java.lang.invoke.stringConcat", "BC_SB"}
            , {"user.country", "US"}
            , {"user.language", "en"}
            , {"org.graalvm.version", "dev"}
            , {"org.graalvm.config", ""}
            , {"com.oracle.graalvm.isaot", "true"} // TODO probably not needed? only used by the binary
            , {"jdk.internal.lambda.disableEagerInitialization", "true"}
            , {"jdk.internal.lambda.eagerlyInitialize", "false"}
            , {"java.lang.invoke.InnerClassLambdaMetafactory.initializeLambdas", "false"}
        }).map(entry -> JavaOptions.systemProperty(entry[0], entry[1]));

        final Stream<String> exports = Stream.of(
            "jdk.internal.vm.ci/jdk.vm.ci.runtime"
            , "jdk.internal.vm.ci/jdk.vm.ci.code"
            , "jdk.internal.vm.ci/jdk.vm.ci.aarch64"
            , "jdk.internal.vm.ci/jdk.vm.ci.amd64"
            , "jdk.internal.vm.ci/jdk.vm.ci.meta"
            , "jdk.internal.vm.ci/jdk.vm.ci.hotspot"
            , "jdk.internal.vm.ci/jdk.vm.ci.services"
            , "jdk.internal.vm.ci/jdk.vm.ci.common"
            , "jdk.internal.vm.ci/jdk.vm.ci.code.site"
            , "jdk.internal.vm.ci/jdk.vm.ci.code.stack"
        ).map(JavaOptions::addUnnamed)
            .flatMap(JavaOptions::addExports);

        final Stream<String> opens = Stream.of(
            "jdk.internal.vm.compiler/org.graalvm.compiler.debug"
            , "jdk.internal.vm.compiler/org.graalvm.compiler.nodes"
            , "jdk.unsupported/sun.reflect"
            , "java.base/jdk.internal.module"
            , "java.base/jdk.internal.ref"
            , "java.base/jdk.internal.reflect"
            , "java.base/java.io"
            , "java.base/java.lang"
            , "java.base/java.lang.reflect"
            , "java.base/java.lang.invoke"
            , "java.base/java.lang.ref"
            , "java.base/java.net"
            , "java.base/java.nio"
            , "java.base/java.nio.file"
            , "java.base/java.security"
            , "java.base/javax.crypto"
            , "java.base/java.util"
            , "java.base/java.util.concurrent.atomic"
            , "java.base/sun.security.x509"
            , "java.base/jdk.internal.logger"
            , "org.graalvm.sdk/org.graalvm.nativeimage.impl"
            , "org.graalvm.sdk/org.graalvm.polyglot"
            , "org.graalvm.truffle/com.oracle.truffle.polyglot"
            , "org.graalvm.truffle/com.oracle.truffle.api.impl"
        ).map(JavaOptions::addUnnamed)
            .flatMap(JavaOptions::addOpens);

        // TODO: add -XX:-UseJVMCICompiler

        final Stream<String> xss = Stream.of("10m").map(JavaOptions::xss);
        final Stream<String> xms = Stream.of("1g").map(JavaOptions::xms);
        final Stream<String> xmx = Stream.of("13743895344").map(JavaOptions::xmx);

        final String graalHome =
            "/Users/g/1/graal-19.3/graal/sdk/mxbuild/darwin-amd64/GRAALVM_LIBGRAAL_JAVA11_BNATIVE-IMAGE_BNATIVE-IMAGE-CONFIGURE_NI_NIC_NIL_NJU_SJVMCICOMPILER_SNATIVE-IMAGE-AGENT/graalvm-libgraal-java11-19.3.1/Contents/Home";

        final Stream<String> modulePath = JavaOptions.modulePath(
            relativeTo("lib/truffle/truffle-api.jar", graalHome)
        );

        final Stream<String> javaAgent = Stream.of(
            relativeTo("lib/svm/builder/svm.jar", graalHome)
        ).map(JavaOptions::javaAgent);

        final Stream<String> classPath = Stream.of(
            relativeTo("lib/svm/builder/llvm-wrapper-shadowed.jar", graalHome)
            , relativeTo("lib/svm/builder/svm.jar", graalHome)
            , relativeTo("lib/svm/builder/objectfile.jar", graalHome)
            , relativeTo("lib/svm/builder/graal-llvm.jar", graalHome)
            , relativeTo("lib/svm/builder/llvm-platform-specific-shadowed.jar", graalHome)
            , relativeTo("lib/svm/builder/javacpp-shadowed.jar", graalHome)
            , relativeTo("lib/svm/builder/svm-llvm.jar", graalHome)
            , relativeTo("lib/svm/builder/pointsto.jar", graalHome)
        );

        final String mainClass = "com.oracle.svm.hosted.NativeImageGeneratorRunner$JDK9Plus";

        final Stream<String> imageCp = Stream.of(
            relativeTo("lib/svm/builder/llvm-wrapper-shadowed.jar", graalHome)
            , relativeTo("lib/svm/builder/svm.jar", graalHome)
            , relativeTo("lib/svm/builder/objectfile.jar", graalHome)
            , relativeTo("lib/svm/builder/graal-llvm.jar", graalHome)
            , relativeTo("lib/svm/builder/llvm-platform-specific-shadowed.jar", graalHome)
            , relativeTo("lib/svm/builder/javacpp-shadowed.jar", graalHome)
            , relativeTo("lib/svm/builder/svm-llvm.jar", graalHome)
            , relativeTo("lib/svm/builder/pointsto.jar", graalHome)
            , relativeTo("lib/svm/library-support.jar", graalHome)
            // Directory of classes, or link to jar(s)
            , "/Users/g/1/jawa/substratevm/helloworld/helloworld.jar"
        );

        final Stream<String> cLibraryPath = Stream.of(
            relativeTo("lib/svm/clibraries/darwin-amd64", graalHome)
            , "/Users/g/1/graal-19.3/graal/substratevm/mxbuild/darwin-amd64/SVM_HOSTED_NATIVE/darwin-amd64" // TODO: what's this?
        );

        final Stream<String> hArguments = Stream.of(new String[][]{
            // Target directory for binary
            {"Path", "/Users/g/1/jawa/substratevm/native-external/target"}
            , {"CLibraryPath", cLibraryPath.collect(NativeImageArguments.cLibraryPath())}
            , {"Class", "HelloWorld"} // TODO: in a jar situation this should be extractable from jar
            , {"Name", "helloworld"}
        }).map(entry -> NativeImageArguments.h(entry[0], entry[1]));

        final List<String> command = Stream.of(
            Stream.of(relativeTo("bin/java", graalHome))
            , xxPlus
            , systemProperties
            , exports
            , opens
            , xss
            , xms
            , xmx
            , modulePath
            , javaAgent
            , JavaOptions.cp()
            , Stream.of(classPath.collect(JavaOptions.colon()))
            , Stream.of(mainClass)
            , NativeImageArguments.imageCp()
            , Stream.of(imageCp.collect(JavaOptions.colon()))
            , hArguments
        ).flatMap(s -> s)
            .collect(Collectors.toList());

        // System.out.println(command);
        execute(command);
    }

    private static void execute(List<String> command) throws Exception {
        Path outputDir = FileSystems.getDefault().getPath("/Users/g/1/graal-19.3/graal/substratevm");
        CountDownLatch errorReportLatch = new CountDownLatch(1);

        System.out.println("Executing: ");
        System.out.println(command);

        Process process = new ProcessBuilder(command)
            .directory(outputDir.toFile())
            .inheritIO()
            .start();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new ErrorReplacingProcessReader(
            process.getErrorStream()
            , outputDir.resolve("reports").toFile()
            , errorReportLatch)
        );
        executor.shutdown();
        errorReportLatch.await();
        if (process.waitFor() != 0) {
            throw new RuntimeException("Image generation failed. Exit code: " + process.exitValue());
        }
    }

    private static String relativeTo(String path, String relativeTo)
    {
        return String.format(
            "%s/%s"
            , relativeTo
            , path
        );
    }
}
