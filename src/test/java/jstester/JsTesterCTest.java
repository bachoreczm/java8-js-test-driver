package jstester;

import static jstester.JsTester.runTestsAndGetErrors;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

public class JsTesterCTest {

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
    assertTrue(stackTraces.contains("at assertEquals (test_util.js:113)"));
    assertTrue(stackTraces.contains("at bug (jstester.source.js:17)"));
    assertTrue(stackTraces
        .contains("at test4 (jstester.test_for_stack_trace.js:20)"));
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
