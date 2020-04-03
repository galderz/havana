package jawa.junit;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.PrintWriter;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class RunJUnit5TestsFromJava
{
    SummaryGeneratingListener summaryListener = new SummaryGeneratingListener();

    public void runOne()
    {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
            .selectors(selectClass(FirstUnitTest.class))
            .build();
        Launcher launcher = LauncherFactory.create();
        launcher.registerTestExecutionListeners(summaryListener);
        launcher.execute(request);
    }

    public static void main(String[] args)
    {
        RunJUnit5TestsFromJava runner = new RunJUnit5TestsFromJava();
        runner.runOne();

        TestExecutionSummary summary = runner.summaryListener.getSummary();
        summary.printTo(new PrintWriter(System.out));
        summary.printFailuresTo(new PrintWriter(System.err));
    }

}
