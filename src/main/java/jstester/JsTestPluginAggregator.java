package jstester;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsTestPluginAggregator implements Iterable<JsTestPlugin> {

  private final DefaultJsTestPlugin defaultPlugin;
  private final List<JsTestPlugin> plugins;

  /**
   * Initialize the default plugin, and the plugins' {@link List}.
   */
  public JsTestPluginAggregator() {
    defaultPlugin = new DefaultJsTestPlugin();
    plugins = new ArrayList<JsTestPlugin>();
  }

  private JsTestPluginAggregator(int initCapacity) {
    defaultPlugin = new DefaultJsTestPlugin();
    plugins = new ArrayList<JsTestPlugin>(initCapacity);
  }

  /**
   * @return an aggregator which contains only the {@link DefaultJsTestPlugin}.
   */
  public static JsTestPluginAggregator empty() {
    return new JsTestPluginAggregator(0);
  }

  /**
   * Adding a new plugin.
   *
   * @param plugin
   *          adding this plugin.
   */
  public void add(JsTestPlugin plugin) {
    plugins.add(plugin);
  }

  /**
   * @return the default plugin.
   */
  public DefaultJsTestPlugin getDefault() {
    return defaultPlugin;
  }

  private boolean hasNextPlugin(int index) {
    if (index == 0) {
      return true;
    }
    return index <= plugins.size();
  }

  private JsTestPlugin nextPlugin(int index) {
    if (index == 1) {
      return defaultPlugin;
    } else {
      JsTestPlugin plugin = plugins.get(index - 2);
      return plugin;
    }
  }

  @Override
  public Iterator<JsTestPlugin> iterator() {
    return new Iterator<JsTestPlugin>() {

      private int index = 0;

      @Override
      public boolean hasNext() {
        return hasNextPlugin(index);
      }

      @Override
      public JsTestPlugin next() {
        ++index;
        return nextPlugin(index);
      }
    };
  }
}
