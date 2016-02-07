package jstester;

import static jstester.JsContentsUtil.readFile;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jstester.exceptions.JsTestException;
import jstester.plugins.JsTestPlugin;
import jstester.plugins.JsTestPluginAggregator;
import jstester.plugins.defaultplugin.JsFileCollection;

/**
 * Javascript source tester util.
 */
public final class JsTester {

  private static final String JS_TEST_UTIL_PATH = "javascript.test.test_util";
  /**
   * The content of the test_util.js
   */
  public static final JsFile JS_TEST_UTIL = readFile(JS_TEST_UTIL_PATH);
  private static final Logger LOGGER = LoggerFactory.getLogger(JsTester.class);

  private JsTester() {
  }

  /**
   * @return new javascript engine.
   */
  public static ScriptEngine newEngine() {
    final ScriptEngineManager manager = new ScriptEngineManager();
    return manager.getEngineByName("JavaScript");
  }

  public static String runTestsAndGetTestResult(String... fileNames) {
    JsFileCollection jsFiles = JsContentsUtil.readFiles(fileNames);
    return runPluginsAndGetTestResults(JsTestPluginAggregator.empty(jsFiles));
  }

  public static String
      runPluginsAndGetTestResults(JsTestPluginAggregator plugins) {
    for (JsTestPlugin plugin : plugins) {
      plugin.eval();
    }
    String lastStackTraces = plugins.getDefault().getLastStackTraces();
    String lastRunResults = plugins.getDefault().getLastRunResults();
    if (!"".equals(lastStackTraces)) {
      LOGGER.error(lastRunResults);
      throw new JsTestException(lastStackTraces);
    }
    return lastRunResults;
  }
}
