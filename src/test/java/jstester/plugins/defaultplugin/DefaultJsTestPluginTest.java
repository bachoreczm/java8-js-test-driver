package jstester.plugins.defaultplugin;

import static org.junit.Assert.assertNull;

import java.io.IOException;

import javax.script.ScriptException;

import jstester.JsFile;

import org.junit.Test;

public class DefaultJsTestPluginTest {

  @Test
  public void testSpeedUp() throws IOException, ScriptException {
    DefaultJsTestPlugin defaultPlugin = new DefaultJsTestPlugin();
    defaultPlugin.eval(new JsFile[0]);
    assertNull(defaultPlugin.getLastStackTraces());
  }
}
