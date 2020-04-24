package nativ.external;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NativeImageJavaHome
{
    public static void main(String[] args) throws Exception
    {
        final boolean isDebug = Boolean.getBoolean("debug");

        final Stream<String> xxPlus = Stream.of(
            "UnlockExperimentalVMOptions"
            , "EnableJVMCI"
        ).map(JavaOptions::xxPlus);

        final Stream<String> xxMinus = Stream.of(
            "UseJVMCICompiler"
        ).map(JavaOptions::xxMinus);

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
            , {"com.oracle.graalvm.isaot", "true"} // TODO could it be set to false? what's the impact?
            , {"jdk.internal.lambda.disableEagerInitialization", "true"}
            , {"jdk.internal.lambda.eagerlyInitialize", "false"}
            , {"java.lang.invoke.InnerClassLambdaMetafactory.initializeLambdas", "false"}
            // Begin 20.1, to avoid:
            // java.lang.ClassCastException: class jdk.internal.loader.ClassLoaders$AppClassLoader
            // cannot be cast to class com.oracle.svm.hosted.NativeImageSystemClassLoader
            // (jdk.internal.loader.ClassLoaders$AppClassLoader is in module java.base of loader 'bootstrap';
            // com.oracle.svm.hosted.NativeImageSystemClassLoader is in unnamed module of loader 'app')
            , {"java.system.class.loader", "com.oracle.svm.hosted.NativeImageSystemClassLoader"}
            // End 20.1
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

        final Stream<String> xss = Stream.of("10m").map(JavaOptions::xss);
        final Stream<String> xms = Stream.of("1g").map(JavaOptions::xms);
        final Stream<String> xmx = Stream.of("13743895344").map(JavaOptions::xmx);


        final String graalHome = System.getProperty("native.graal.home");

        final String mavenHome =
            "/Users/g/.m2/repository";

        final String graalVersion = "20.2.0-SNAPSHOT";
        final Function<String, String> mavenPath = relativeTo(mavenHome).compose(mavenVersioned(graalVersion));
        // $ cd substratevm && mx build && mx maven-install
        final String jarSvm = mavenPath.apply("org/graalvm/nativeimage/svm/%1$s/svm-%1$s.jar");
        final String jarObjectFile = mavenPath.apply("org/graalvm/nativeimage/objectfile/%1$s/objectfile-%1$s.jar");
        final String jarPointsTo = mavenPath.apply("org/graalvm/nativeimage/pointsto/%1$s/pointsto-%1$s.jar");
        // $ cd truffle && mx maven-install
        final String jarTruffleApi = mavenPath.apply("org/graalvm/truffle/truffle-api/%1$s/truffle-api-%1$s.jar");
        // $ cd sdk && mx maven-install
        final String jarGraalSdk = mavenPath.apply("org/graalvm/sdk/graal-sdk/%1$s/graal-sdk-%1$s.jar");
        // $ cd compiler && mx maven-install
        final String jarCompiler = mavenPath.apply("org/graalvm/compiler/compiler/%1$s/compiler-%1$s.jar");

        final Stream<String> modulePath = Stream.of(
            jarTruffleApi
            , jarGraalSdk
            , jarCompiler
        );

        final Stream<String> upgradeModulePath = Stream.of(
            jarCompiler
        );


        final Stream<String> javaAgent = Stream.of(
            jarSvm
        ).map(JavaOptions::javaAgent);


        final Stream<String> classPath = Stream.of(
            jarObjectFile
            , jarPointsTo
            , jarSvm
        );

        final String mainClass = "com.oracle.svm.hosted.NativeImageGeneratorRunner$JDK9Plus";

        final Stream<String> imageCp = Stream.of(
            jarObjectFile
            , jarPointsTo
            , jarSvm

            // With GraalVM home based setup, there doesn't seem to be a need for these jars here,
            // because there native-image-modules.list seems to trigger early class loading,
            // and annotaton processing.
            // Without that list file, we just force the jars through as part of the imagecp
            , jarCompiler
            , jarGraalSdk

            // Directory of classes, or link to jar(s)
            // , "/Users/g/1/jawa/substratevm/daytwo/daytwo.jar"
            , "/Users/g/1/jawa/substratevm/helloworld/helloworld.jar"
        );

        final Stream<String> cLibraryPath = Stream.of(
            // "/opt/graal-graal-clibraries/darwin-amd64"
            relativeTo(graalHome).apply("lib/svm/clibraries/darwin-amd64")
        );

        final Stream<String> hArguments = Stream.of(new String[][]{
            // Target directory for binary
            {"Path", "/Users/g/1/jawa/substratevm/native-external/target"}
            , {"CLibraryPath", cLibraryPath.collect(Collectors.joining(","))}
            // , {"Class", "DayTwo"} // TODO: in a jar situation this should be extractable from jar?
            // , {"Name", "daytwo"}
            , {"Class", "HelloWorld"} // TODO: in a jar situation this should be extractable from jar?
            , {"Name", "helloworld"}
        }).map(entry -> NativeImageArguments.h(entry[0], entry[1]));

        final Stream<String> javaBin = Stream.of(
            relativeTo(System.getProperty("native.java.home"))
                .apply("bin/java")
        );

        final Stream<String> debug = Stream.of(
            isDebug ? "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000" : ""
        );

        final List<String> command = Stream.of(
            javaBin
            , debug
            , xxPlus
            , xxMinus
            , systemProperties
            , exports
            , opens
            , xss
            , xms
            , xmx
            , Stream.of("--add-modules", "org.graalvm.truffle,org.graalvm.sdk")
            , JavaOptions.modulePath()
            , Stream.of(modulePath.collect(JavaOptions.colon()))
            , Stream.of("--upgrade-module-path")
            , Stream.of(upgradeModulePath.collect(JavaOptions.colon()))
            , javaAgent
            , JavaOptions.cp()
            , Stream.of(classPath.collect(JavaOptions.colon()))
            , Stream.of(mainClass)
            , NativeImageArguments.imageCp()
            , Stream.of(imageCp.collect(JavaOptions.colon()))
            , hArguments
            , Stream.of("-H:+ReportExceptionStackTraces")
        ).flatMap(s -> s)
            .collect(
                ArrayList::new
                , (list, entry) ->
                {
                    if (!entry.isEmpty())
                        list.add(entry);
                }
                , ArrayList::addAll
            );

        // System.out.println(command);
        execute(command);
    }

    private static void execute(List<String> command) throws Exception
    {
        Path outputDir = FileSystems.getDefault().getPath("/Users/g/1/graal-graal/graal/substratevm");
        CountDownLatch errorReportLatch = new CountDownLatch(1);

        System.out.println("Executing: ");
        System.out.println(command);
        System.out.println("Executing (command line)");
        System.out.println(command.stream().collect(Collectors.joining(" ")));

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
        if (process.waitFor() != 0)
        {
            throw new RuntimeException("Image generation failed. Exit code: " + process.exitValue());
        }
    }

    private static Function<String, String> mavenVersioned(String version)
    {
        return mavenPathFormat ->
            String.format(
                mavenPathFormat
                , version
            );
    }

    private static Function<String, String> relativeTo(String relativeTo)
    {
        return path ->
            String.format(
                "%s/%s"
                , relativeTo
                , path
            );
    }
}