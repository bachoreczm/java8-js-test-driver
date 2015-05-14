package jstester.stylechecker;

import static jstester.JsTester.computeUserCode;
import static jstester.JsTester.newEngine;

import java.io.IOException;

import javax.script.ScriptException;

import jstester.JsFileProperties;
import jstester.JsTestPlugin;

public class StyleChecker implements JsTestPlugin {

  private static final String USE_STRICT = "\"use strict\";";
  private static final int DEFAULT_MAX_LINE_LENGTH = 80;
  private static final boolean DEFAULT_USE_STRICT_MODE = true;

  private boolean strictMode = DEFAULT_USE_STRICT_MODE;
  private int maxLineLength = DEFAULT_MAX_LINE_LENGTH;

  private StringBuilder styleErrors;

  private StyleRule lineLengthChecker;
  private StyleRule curlyBracesChecker;

  @Override
  public void eval(JsFileProperties[] userCodes) throws IOException,
      ScriptException {
    init();
    styleErrors.append(lineLengthChecker.checkRule(userCodes));
    styleErrors.append(curlyBracesChecker.checkRule(userCodes));
    String userCode = computeUserCode(userCodes);
    String code = computeUseStrictCode(userCode);
    try {
      newEngine().eval(code);
    } catch (ScriptException ex) {
      styleErrors.append(format(ex.getMessage(), userCodes) + "\n");
    }
  }

  private void init() {
    styleErrors = new StringBuilder();
    lineLengthChecker = new LineLengthChecker(maxLineLength);
    curlyBracesChecker = new CurlyBracesChecker();
  }

  private String format(final String msg, final JsFileProperties[] userCodes) {
    String[] splittedMsg = msg.split("line number ");
    int lineNum = Integer.parseInt(splittedMsg[1]);
    if (strictMode) {
      lineNum -= 1;
    }
    int codeIndex = 0;
    while (lineNum - userCodes[codeIndex].getLineNumbers() - 1 > 0) {
      lineNum -= userCodes[codeIndex].getLineNumbers() + 1;
      ++codeIndex;
    }
    String actualFileName = userCodes[codeIndex].getFileName();
    String firstPart = splittedMsg[0];
    String filenameMsg = firstPart.replace("<eval>", actualFileName);
    return filenameMsg + "line number " + lineNum + ".";
  }

  private String computeUseStrictCode(String userCode) {
    if (strictMode) {
      return USE_STRICT + "\n" + userCode;
    } else {
      return userCode;
    }
  }

  /**
   * If there is style errors, this method throws exception, and the errors is
   * in its message.
   */
  public void styleErrors() {
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
}
