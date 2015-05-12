package jstester.stylechecker;

import jstester.JsFileProperties;

public interface StyleRule {

  /**
   * @param userCodes
   *          the js files' content
   * @return the style errors in {@link String}.
   */
  String checkRule(JsFileProperties[] userCodes);
}
