package jstester;

import static jstester.JsTester.JS_TEST_UTIL;
import static jstester.JsTester.runTestsAndGetErrors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

import general.TestUtil;
import jstester.exceptions.JsTestException;
import jstester.plugins.JsTestPluginAggregator;

public class JsTesterTest {

  @Test
  public void testPrivateConstructor() throws ReflectiveOperationException {
    TestUtil.assertHasPrivateConstructor(JsTester.class);
  }

  @Test
  public void testConstants() {
    assertNotNull(JsTester.newEngine());
    assertEquals("javascript.test.test_util", JS_TEST_UTIL);
  }

  @Test
  public void testSimpleJsTest() throws IOException, ScriptException {
    String testFile = "jstester.simple_test";
    String srcFile = "jstester.source";
    runTestsAndGetErrors(testFile, srcFile);
  }

  @Test
  public void testAssertsJsTest() throws IOException, ScriptException {
    String testFile = "jstester.test_for_assertations";
    assertEquals("", getStackTrace(testFile));
  }

  @Test
  public void testStackTrace() throws IOException, ScriptException {
    String testFile = "jstester.test_for_stack_trace";
    String srcFile = "jstester.source";
    String stackTraces = getStackTrace(testFile, srcFile);
    assertTrue(stackTraces.contains("AssertationError"));
    assertTrue(stackTraces.contains("at assertEquals (test_util.js:44)"));
    assertTrue(stackTraces,
        stackTraces.contains("at bug (jstester.source.js:17)"));
    assertTrue(
        stackTraces.contains("at test4 (jstester.test_for_stack_trace.js:20)"));
  }

  @Test
  public void testSkip() throws IOException {
    String testFile = "jstester.skip_some_tests";
    runTestsAndGetErrors(testFile);
  }

  @Test
  public void testLog() throws IOException {
    String testFile = "jstester.test_for_log";
    JsTestPluginAggregator aggregator = JsTestPluginAggregator.empty();
    runTestsAndGetErrors(aggregator, testFile);
    String expected = "this is a log message...\n";
    assertEquals(expected, aggregator.getDefault().getLog());
  }

  @Test
  public void testMultiSourceStackTrace() throws IOException {
    String testFile = "jstester.test_for_multi_source";
    String srcFile1 = "jstester.source";
    String srcFile2 = "jstester.caller";
    String stackTraces = getStackTrace(testFile, srcFile1, srcFile2);
    assertTrue(stackTraces,
        stackTraces.contains("TypeError: Cannot call undefined"));
    assertTrue(stackTraces.contains("at bug (jstester.source.js:17)"));
    assertTrue(stackTraces.contains("at callBug (jstester.caller.js:6)"));
    assertTrue(stackTraces
        .contains("at testCallBug (jstester.test_for_multi_source.js:5)"));
  }

  private String getStackTrace(String... codes) throws IOException {
    try {
      runTestsAndGetErrors(codes);
      return "";
    } catch (JsTestException ex) {
      return ex.getMessage();
    }
  }
}
