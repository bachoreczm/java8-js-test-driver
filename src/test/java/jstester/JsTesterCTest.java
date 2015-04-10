package jstester;

import static jstester.JsTester.runtestsAndGetErrors;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

public class JsTesterCTest {

  @Test
  public void testSimpleJsTest() throws IOException, ScriptException {
    String testFile = "jstester.simple_test";
    String srcFile = "jstester.source";
    assertTrue(runtestsAndGetErrors(testFile, srcFile).equals(""));
  }

  @Test
  public void testAssertsJsTest() throws IOException, ScriptException {
    String testFile = "jstester.test_for_assertations";
    assertTrue(runtestsAndGetErrors(testFile).equals(""));
  }

  @Test
  public void testStackTrace() throws IOException, ScriptException {
    String testFile = "jstester.test_for_stack_trace";
    String srcFile = "jstester.source";
    String stackTraces = runtestsAndGetErrors(testFile, srcFile);
    assertTrue(stackTraces.contains("AssertationError"));
    assertTrue(stackTraces.contains("at assertEquals (test_util.js:113)"));
    assertTrue(stackTraces.contains("at bug (jstester.source.js:17)"));
    assertTrue(stackTraces
        .contains("at test4 (jstester.test_for_stack_trace.js:20)"));
  }
}
