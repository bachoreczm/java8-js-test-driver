package jstester.plugins.stylechecker;

import jstester.plugins.defaultplugin.JsFileCollection;

public interface StyleRule {

  /**
   * @param userCodes
   *          the js files' content
   * @return the style errors in {@link String}.
   */
  String checkRule(JsFileCollection userCodes);
}
