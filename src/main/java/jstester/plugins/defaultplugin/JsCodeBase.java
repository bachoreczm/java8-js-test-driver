package jstester.plugins.defaultplugin;

import java.util.ArrayList;
import java.util.List;

import jstester.JsFile;

final class JsCodeBase {

  private static final String EVAL = "<eval>";
  private final JsFile testUtil;
  private final JsFile[] userCodes;

  JsCodeBase(JsFile testUtilProps, JsFile... sourceProps) {
    testUtil = testUtilProps;
    userCodes = sourceProps;
  }

  String formatStacktraceRow(String row) {
    final String formattedAssertion = formatAssertationCalls(row);
    return formatUserRow(formattedAssertion);
  }

  private static String formatAssertationCalls(final String row) {
    for (String assertation : getAsserts()) {
      if (row.trim().startsWith("at " + assertation)) {
        return row.replace(EVAL, "test_util.js");
      }
    }
    return row;
  }

  private static List<String> getAsserts() {
    final List<String> assertations = new ArrayList<String>(3);
    assertations.add("assertEquals");
    assertations.add("assertTrue");
    assertations.add("assertFalse");
    return assertations;
  }

  private String formatUserRow(final String row) {
    if (row.contains(EVAL)) {
      final String partOne = row.split(":")[0];
      final String partTwo = row.split(":")[1];
      final String lineNumString = partTwo.substring(0, partTwo.length() - 1);
      int lineNum = Integer.parseInt(lineNumString);
      lineNum -= testUtil.getLineNumbers() + 1;
      int codeIndex = 0;
      while (canDecraseLineNum(lineNum, codeIndex)) {
        lineNum -= userCodes[codeIndex].getLineNumbers() + 1;
        ++codeIndex;
      }
      final String line = partOne + ":" + lineNum + ")";
      return line.replace(EVAL, getJsFileName(codeIndex));
    } else {
      return row;
    }
  }

  private String getJsFileName(int codeIndex) {
    return userCodes[codeIndex].getFileName() + ".js";
  }

  private boolean canDecraseLineNum(int lineNum, int codeIndex) {
    int lineNumbers = userCodes[codeIndex].getLineNumbers();
    return lineNum - lineNumbers - 1 > 0;
  }
}
