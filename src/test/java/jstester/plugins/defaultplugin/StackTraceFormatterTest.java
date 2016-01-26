package jstester.plugins.defaultplugin;

import general.TestUtil;

import org.junit.Test;

public class StackTraceFormatterTest {

  @Test
  public void testStackTraceFormatterPrivateConstructor()
      throws ReflectiveOperationException {
    TestUtil.assertHasPrivateConstructor(StackTraceFormatter.class);
  }
}
