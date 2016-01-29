package jstester.plugins.defaultplugin;

import java.util.ArrayList;
import java.util.List;

final class StackTraceFormatter {

  private static final String EVAL = "<eval>";

  private StackTraceFormatter() {
  }

  private static List<String> getAsserts() {
    final List<String> assertations = new ArrayList<String>(3);
    assertations.add("assertEquals");
    assertations.add("assertTrue");
    assertations.add("assertFalse");
    return assertations;
  }

  static String format(Stacktraces stackTraces,
      StackTraceProperties stackProps) {
    return formatTraceRows(stackTraces, stackProps);
  }

  private static String formatTraceRows(Stacktraces stacktraces,
      StackTraceProperties stackProps) {
    StringBuilder formattedRows = new StringBuilder();
    int i = 0;
    for (Stacktrace currentStacktrace : stacktraces) {
      if (i > 0) {
        formattedRows.append("\n");
      }
      formattedRows.append(formatOneStackTrace(stackProps, currentStacktrace));
      ++i;
    }
    return formattedRows.toString();
  }

  private static String formatOneStackTrace(StackTraceProperties stackProps,
      Stacktrace stacktrace) {
    boolean needNewLine = false;
    StringBuilder formattedRows = new StringBuilder();
    for (String row : stacktrace) {
      if (needNewLine) {
        formattedRows.append("\n");
      }
      if (notTestUtilStack(row)) {
        final String formattedAssertion = formatAssertationCalls(row);
        final String formattedUserRow = formatUserRow(formattedAssertion,
            stackProps);
        formattedRows.append(formattedUserRow);
        needNewLine = true;
      } else {
        needNewLine = false;
      }
    }
    return formattedRows.toString();
  }

  private static boolean notTestUtilStack(final String row) {
    return !row.trim().startsWith("at <program>")
        && !row.trim().startsWith("at runAllTests");
  }

  private static String formatUserRow(final String row,
      StackTraceProperties stackProps) {
    if (row.contains(EVAL)) {
      final String partOne = row.split(":")[0];
      final String partTwo = row.split(":")[1];
      final String lineNumString = partTwo.substring(0, partTwo.length() - 1);
      int lineNum = Integer.parseInt(lineNumString);
      lineNum -= stackProps.getTestUtil().getLineNumbers() + 1;
      int codeIndex = 0;
      while (canDecraseLineNum(stackProps, lineNum, codeIndex)) {
        lineNum -= stackProps.getCodeByIndex(codeIndex).getLineNumbers() + 1;
        ++codeIndex;
      }
      final String line = partOne + ":" + lineNum + ")";
      return line.replace(EVAL, getJsFile(stackProps, codeIndex));
    } else {
      return row;
    }
  }

  private static String getJsFile(StackTraceProperties props, int codeIndex) {
    return props.getCodeByIndex(codeIndex).getFileName() + ".js";
  }

  private static boolean canDecraseLineNum(StackTraceProperties stackProps,
      int lineNum, int codeIndex) {
    int lineNumbers = stackProps.getCodeByIndex(codeIndex).getLineNumbers();
    return lineNum - lineNumbers - 1 > 0;
  }

  private static String formatAssertationCalls(final String row) {
    for (String assertation : getAsserts()) {
      if (row.trim().startsWith("at " + assertation)) {
        return row.replace(EVAL, "test_util.js");
      }
    }
    return row;
  }
}
