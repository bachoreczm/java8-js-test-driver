package jstester.plugins;

/**
 * Plugin for {@link jstester.JsTester}. You can run js codes with it, then you
 * can get properties of the last evaluation.
 */
public interface JsTestPlugin {

  /**
   * Do something with the given codes (specially run in
   * {@link jstester.JsTester#ENGINE} ), and set some field.
   */
  void eval();

  /**
   * @return the name of the plugin.
   */
  String getName();

  /**
   * @return the results of the last run.
   */
  String getLastRunResults();
}
