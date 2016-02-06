package jstester.plugins.defaultplugin;

import static jstester.JsTester.JS_TEST_UTIL;

import java.util.ArrayList;
import java.util.List;

import jstester.JsFile;

final class JsCodeBase {

  private static final String EVAL = "<eval>";
  private final JsFile[] userCodes;

  JsCodeBase(JsFile... sourceProps) {
    userCodes = sourceProps;
  }

  String formatStacktraceRow(StacktraceRow row) {
    final String formattedAssertion = formatAssertationCallsOf(row);
    return formatUserRow(formattedAssertion);
  }

  private static String formatAssertationCallsOf(final StacktraceRow row) {
    for (String assertation : getAsserts()) {
      if (row.startsWithAtAssertion(assertation)) {
        return row.withTheFileNameOfTestUtil();
      }
    }
    return row.asString();
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
      lineNum -= JS_TEST_UTIL.getLineNumbers() + 1;
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
