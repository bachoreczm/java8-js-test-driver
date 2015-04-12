package jstester;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

public class DefaultJsTestPluginTest {

  @Test
  public void testSpeedUp() throws IOException, ScriptException {
    DefaultJsTestPlugin defaultPlugin = new DefaultJsTestPlugin();
    defaultPlugin.eval(new JsFileProperties[0]);
    assertNull(defaultPlugin.getLastStackTraces());
  }
}
