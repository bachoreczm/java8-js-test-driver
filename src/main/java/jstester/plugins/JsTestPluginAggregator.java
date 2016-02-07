package jstester.plugins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jstester.plugins.defaultplugin.DefaultJsTestPlugin;
import jstester.plugins.defaultplugin.JsFileCollection;

public class JsTestPluginAggregator implements Iterable<JsTestPlugin> {

  private final DefaultJsTestPlugin defaultPlugin;
  private final List<JsTestPlugin> plugins;

  /**
   * Initialize the default plugin, and the plugins' {@link List}.
   *
   * @param jsFiles
   *          the collection of the usercodes.
   */
  public JsTestPluginAggregator(JsFileCollection jsFiles) {
    defaultPlugin = new DefaultJsTestPlugin(jsFiles);
    plugins = new ArrayList<JsTestPlugin>();
  }

  private JsTestPluginAggregator(int initCapacity, JsFileCollection jsFiles) {
    defaultPlugin = new DefaultJsTestPlugin(jsFiles);
    plugins = new ArrayList<JsTestPlugin>(initCapacity);
  }

  /**
   * @param jsFiles
   *          the source and test files
   * @return an aggregator which contains only the {@link DefaultJsTestPlugin}.
   */
  public static JsTestPluginAggregator empty(JsFileCollection jsFiles) {
    return new JsTestPluginAggregator(0, jsFiles);
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
      return plugins.get(index - 2);
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
