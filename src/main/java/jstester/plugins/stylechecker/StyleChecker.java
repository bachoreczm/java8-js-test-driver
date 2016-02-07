package jstester.plugins.stylechecker;

import static jstester.JsTester.newEngine;

import javax.script.ScriptException;

import jstester.plugins.JsTestPlugin;
import jstester.plugins.defaultplugin.JsFileCollection;

public class StyleChecker implements JsTestPlugin {

  private static final String USE_STRICT = "\"use strict\";";
  private static final int DEFAULT_MAX_LINE_LENGTH = 80;
  private static final boolean DEFAULT_USE_STRICT_MODE = true;

  private JsFileCollection userCodes;

  private boolean strictMode = DEFAULT_USE_STRICT_MODE;
  private int maxLineLength = DEFAULT_MAX_LINE_LENGTH;

  private StringBuilder styleErrors;

  private StyleRule lineLengthChecker;
  private StyleRule curlyBracesChecker;

  /**
   * Initialize the style-checker, which tries to find style error in the given
   * js-code.
   *
   * @param userCodes
   *          the collection of the usercodes.
   */
  public StyleChecker(JsFileCollection userCodes) {
    this.userCodes = userCodes;
  }

  @Override
  public void eval() {
    init();
    styleErrors.append(lineLengthChecker.checkRule(userCodes));
    styleErrors.append(curlyBracesChecker.checkRule(userCodes));
    String code = computeUseStrictCode();
    try {
      newEngine().eval(code);
    } catch (ScriptException ex) {
      styleErrors.append(userCodes.formatException(ex, strictMode) + "\n");
    }
  }

  private void init() {
    styleErrors = new StringBuilder();
    lineLengthChecker = new LineLengthChecker(maxLineLength);
    curlyBracesChecker = new CurlyBracesChecker();
  }

  private String computeUseStrictCode() {
    String rawContent = userCodes.computeContent();
    if (strictMode) {
      return USE_STRICT + "\n" + rawContent;
    } else {
      return rawContent;
    }
  }

  /**
   * If there is style errors, this method throws exception, and the errors is
   * in its message.
   */
  public void assertForNoStyleErrors() {
    String errors = styleErrors.toString();
    if (!"".equals(errors)) {
      throw new StyleError(errors);
    }
  }

  /**
   * Switch on strict mode.
   */
  public void enableStrictMode() {
    strictMode = true;
  }

  /**
   * Switch off strict mode.
   */
  public void disableStrictMode() {
    strictMode = false;
  }

  public static class StyleError extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * @param msg
     *          exception message.
     */
    public StyleError(String msg) {
      super(msg);
    }
  }

  @Override
  public String getName() {
    return "Stylechecker";
  }

  @Override
  public String getLastRunResults() {
    return styleErrors.toString();
  }
}
