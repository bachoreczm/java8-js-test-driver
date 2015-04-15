package jstester;

import static jstester.JsTester.computeUserCode;
import static jstester.JsTester.newEngine;

import java.io.IOException;

import javax.script.ScriptException;

public class StyleChecker implements JsTestPlugin {

  private static final String USE_STRICT = "\"use strict\";";
  private static final int DEFAULT_MAX_LINE_LENGTH = 80;
  private static final boolean DEFAULT_USE_STRICT_MODE = true;

  private boolean strictMode = DEFAULT_USE_STRICT_MODE;
  private int maxLineLength = DEFAULT_MAX_LINE_LENGTH;

  private StringBuilder styleErrors;

  @Override
  public void eval(JsFileProperties[] userCodes) throws IOException,
      ScriptException {
    init();
    checkLineLength(userCodes);
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
  }

  private void checkLineLength(JsFileProperties[] userCodes) {
    for (int i = 0; i < userCodes.length; ++i) {
      String[] actualFileRows = userCodes[i].toString().split("\n");
      String actualFile = userCodes[i].getFileName();
      for (int j = 0; j < actualFileRows.length; ++j) {
        if (actualFileRows[j].length() > maxLineLength) {
          String err = "Line too long (" + actualFile + ":" + (j + 1) + ").\n";
          styleErrors.append(err);
        }
      }
    }
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
   * @return the style errors in {@link String}, if no errors found, then return
   *         an empty string.
   */
  public String getStyleErrors() {
    return styleErrors.toString();
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
}
