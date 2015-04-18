package jstester;

import static jstester.JsTester.JS_TEST_UTIL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static jstester.JsTester.runTestsAndGetErrors;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

public class JsTesterTest {

  @Test
  public void testConstants() {
    assertNotNull(JsTester.newEngine());
    assertEquals("javascript.test.test_util", JS_TEST_UTIL);
  }

  @Test
  public void testSimpleJsTest() throws IOException, ScriptException {
    String testFile = "jstester.simple_test";
    String srcFile = "jstester.source";
    assertTrue(runTestsAndGetErrors(testFile, srcFile).equals(""));
  }

  @Test
  public void testAssertsJsTest() throws IOException, ScriptException {
    String testFile = "jstester.test_for_assertations";
    assertTrue(runTestsAndGetErrors(testFile).equals(""));
  }

  @Test
  public void testStackTrace() throws IOException, ScriptException {
    String testFile = "jstester.test_for_stack_trace";
    String srcFile = "jstester.source";
    String stackTraces = runTestsAndGetErrors(testFile, srcFile);
    assertTrue(stackTraces.contains("AssertationError"));
    assertTrue(stackTraces.contains("at assertEquals (test_util.js:114)"));
    assertTrue(stackTraces.contains("at bug (jstester.source.js:17)"));
    assertTrue(stackTraces
        .contains("at test4 (jstester.test_for_stack_trace.js:20)"));
  }

  @Test
  public void testSkip() throws IOException, ScriptException {
    String testFile = "jstester.skip_some_tests";
    String stackTraces = runTestsAndGetErrors(testFile);
    assertEquals("", stackTraces);
  }

  @Test
  public void testLog() throws IOException, ScriptException {
    String testFile = "jstester.test_for_log";
    JsTestPluginAggregator aggregator = JsTestPluginAggregator.empty();
    String stackTraces = runTestsAndGetErrors(aggregator, testFile);
    assertEquals("", stackTraces);
    String expected = "this is a log message...\n";
    assertEquals(expected, aggregator.getDefault().getLog());
  }

  @Test
  public void testMultiSourceStackTrace() throws IOException, ScriptException {
    String testFile = "jstester.test_for_multi_source";
    String srcFile1 = "jstester.source";
    String srcFile2 = "jstester.caller";
    String stackTraces = runTestsAndGetErrors(testFile, srcFile1, srcFile2);
    assertTrue(stackTraces.contains("TypeError: Cannot call undefined"));
    assertTrue(stackTraces.contains("at bug (jstester.source.js:17)"));
    assertTrue(stackTraces.contains("at callBug (jstester.caller.js:6)"));
    assertTrue(stackTraces
        .contains("at testCallBug (jstester.test_for_multi_source.js:5)"));
  }
}
