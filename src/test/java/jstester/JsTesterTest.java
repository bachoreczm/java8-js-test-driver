package jstester;

import static jstester.JsTester.runPluginsAndGetTestResults;
import static jstester.JsTester.runTestsAndGetTestResult;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import jstester.exceptions.JsTestException;
import jstester.plugins.JsTestPluginAggregator;
import jstester.plugins.defaultplugin.JsFileCollection;

public class JsTesterTest {

  @Test
  public void assertThatNewEngineReturnsANotNullNewEngine() {
    assertNotNull(JsTester.newEngine());
    assertNotSame(JsTester.newEngine(), JsTester.newEngine());
  }

  @Test
  public void simpleJsTestRunsSuccessfully() {
    String testFile = "jstester.simple_test";
    String srcFile = "jstester.source";
    runTestsAndGetTestResult(testFile, srcFile);
  }

  @Test
  public void theSequenceOfTheFilesDoesntMatter() {
    String testFile = "jstester.simple_test";
    String srcFile = "jstester.source";
    runTestsAndGetTestResult(testFile, srcFile);
    runTestsAndGetTestResult(srcFile, testFile);
  }

  @Test
  public void assertionsTestRunsSuccessfully() {
    runTestsAndGetTestResult("jstester.test_for_assertions");
  }

  @Test
  public void testStackTrace() {
    String testFile = "jstester.test_for_stack_trace";
    String srcFile = "jstester.source";
    String stackTraces = getStackTrace(testFile, srcFile);
    assertTrue(stackTraces.contains("AssertationError"));
    assertTrue(stackTraces.contains("at assertEquals (test_util.js:44)"));
    assertTrue(stackTraces.contains("at bug (jstester.source.js:17)"));
    assertTrue(
        stackTraces.contains("at test4 (jstester.test_for_stack_trace.js:20)"));
  }

  @Test
  public void skipTestRunsSuccessFullyBecauseFailingTestIsSkipped() {
    runTestsAndGetTestResult("jstester.skip_some_tests");
  }

  @Test
  public void testLog() {
    String testFileName = "jstester.test_for_log";
    JsFile testFile = JsContentsUtil.readFile(testFileName);
    JsFileCollection codes = new JsFileCollection(testFile);
    JsTestPluginAggregator aggregator = JsTestPluginAggregator.empty(codes);
    runPluginsAndGetTestResults(aggregator);
    String expected = "this is a log message...\n";
    assertEquals(expected, aggregator.getDefault().getLog());
  }

  @Test
  public void testMultiSourceStackTrace() throws IOException {
    String testFile = "jstester.test_for_multi_source";
    String srcFile1 = "jstester.source";
    String srcFile2 = "jstester.caller";
    String stackTraces = getStackTrace(testFile, srcFile1, srcFile2);
    assertTrue(stackTraces.contains("TypeError: Cannot call undefined"));
    assertTrue(stackTraces.contains("at bug (jstester.source.js:17)"));
    assertTrue(stackTraces.contains("at callBug (jstester.caller.js:6)"));
    assertTrue(stackTraces
        .contains("at testCallBug (jstester.test_for_multi_source.js:5)"));
  }

  private String getStackTrace(String... codes) {
    try {
      runTestsAndGetTestResult(codes);
      return "";
    } catch (JsTestException ex) {
      return ex.getMessage();
    }
  }
}
