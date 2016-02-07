package jstester.plugins;

import java.io.IOException;

import jstester.JsTester;

/**
 * Plugin for {@link JsTester}. You can run js codes with it, then you can get
 * properties of the last evaluation.
 */
public interface JsTestPlugin {

  /**
   * Do something with the given codes (specially run in {@link JsTester#ENGINE}
   * ), and set some field.
   *
   * @param userCodes
   *          the javascript codes.
   * @throws IOException
   *           if an I/O error occurs
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
