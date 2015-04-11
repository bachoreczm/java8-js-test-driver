package jstester;

import java.io.IOException;

import javax.script.ScriptException;

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
   *          the javascript code.
   * @throws IOException
   *           if an I/O error occurs
   * @throws ScriptException
   *           if an error occurs during the script eval.
   */
  void eval(JsFileProperties[] userCodes) throws IOException, ScriptException;
}
