package jstester;

import static jstester.JsTester.ENGINE;
import static jstester.JsTester.JS_TEST_UTIL;
import static org.junit.Assert.*;

import org.junit.Test;

public class JsTesterTest {

  @Test
  public void testConstants() {
    assertNotNull(ENGINE);
    assertEquals("javascript.test.test_util", JS_TEST_UTIL);
  }
}
