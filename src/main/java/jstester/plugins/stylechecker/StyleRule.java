package jstester.plugins.stylechecker;

import jstester.JsFile;

public interface StyleRule {

  /**
   * @param userCodes
   *          the js files' content
   * @return the style errors in {@link String}.
   */
  String checkRule(JsFile[] userCodes);
}
