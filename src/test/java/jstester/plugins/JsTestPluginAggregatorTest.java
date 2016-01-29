package jstester.plugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;

import javax.script.ScriptException;

import jstester.JsFile;
import jstester.plugins.defaultplugin.DefaultJsTestPlugin;

import org.junit.Test;

public class JsTestPluginAggregatorTest {

  @Test
  public void testInit() {
    JsTestPluginAggregator aggregator = new JsTestPluginAggregator();
    assertEquals(DefaultJsTestPlugin.class, aggregator.getDefault().getClass());
    assertNotNull(aggregator.getDefault());
    assertNotNull(JsTestPluginAggregator.empty());
  }

  @Test
  public void testAddAndIterate() throws IOException, ScriptException {
    JsTestPlugin plugin1 = new PluginMock();
    JsTestPlugin plugin2 = new PluginMock();
    JsTestPluginAggregator aggregator = new JsTestPluginAggregator();
    aggregator.add(plugin1);
    aggregator.add(plugin2);
    for (JsTestPlugin plugin : aggregator) {
      plugin.eval(new JsFile[0]);
    }
    assertEquals(1, ((PluginMock) plugin1).getCalled());
    assertEquals(1, ((PluginMock) plugin2).getCalled());
  }

  @Test
  public void testHasNextPlugin() {
    JsTestPluginAggregator empty = JsTestPluginAggregator.empty();
    Iterator<JsTestPlugin> iterator = empty.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(DefaultJsTestPlugin.class, iterator.next().getClass());
    assertFalse(iterator.hasNext());
  }
}
