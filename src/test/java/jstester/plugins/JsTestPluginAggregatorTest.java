package jstester.plugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.Iterator;

import javax.script.ScriptException;

import org.junit.Test;

import jstester.JsFile;
import jstester.plugins.defaultplugin.DefaultJsTestPlugin;

public class JsTestPluginAggregatorTest {

  @Test
  public void theDefaultPluginIsNeverNull() {
    JsTestPluginAggregator aggregator = new JsTestPluginAggregator();

    assertEquals(DefaultJsTestPlugin.class, aggregator.getDefault().getClass());
  }

  @Test
  public void theEmptyAggregatorContainsDefaultPluginAndNoOther() {
    JsTestPluginAggregator empty = JsTestPluginAggregator.empty();

    containsDefaultAndNoOther(empty);
  }

  private void containsDefaultAndNoOther(JsTestPluginAggregator empty) {
    assertEquals(DefaultJsTestPlugin.class, empty.getDefault().getClass());
    Iterator<JsTestPlugin> iterator = empty.iterator();
    iterator.next();
    assertFalse(iterator.hasNext());
  }

  @Test
  public void addFunctionAddsPlugins() throws IOException, ScriptException {
    PluginMock plugin1 = new PluginMock();
    PluginMock plugin2 = new PluginMock();
    JsTestPluginAggregator aggregator = new JsTestPluginAggregator();
    aggregator.add(plugin1);
    aggregator.add(plugin2);

    for (JsTestPlugin plugin : aggregator) {
      plugin.eval(new JsFile[0]);
    }

    assertEquals(1, plugin1.getCalled());
    assertEquals(1, plugin2.getCalled());
  }
}
