package jstester;

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

  static String formattingStackTraces(String stackTraces,
      StackTraceProperties stackProps) {
    return formatTraceRows(stackTraces, stackProps);
  }

  private static String formatTraceRows(String stackTraces,
      StackTraceProperties stackProps) {
    String[] stacks = stackTraces.split("\\n\\n");
    StringBuilder formattedRows = new StringBuilder();
    for (int i = 0; i < stacks.length; ++i) {
      if (i > 0) {
        formattedRows.append("\n");
      }
      String[] rows = getStackByIndex(stacks, i);
      formattedRows.append(formatOneStackTrace(stackProps, rows));
    }
    return formattedRows.toString();
  }

  private static String formatOneStackTrace(StackTraceProperties stackProps,
      String[] rows) {
    boolean needNewLine = false;
    StringBuilder formattedRows = new StringBuilder();
    for (int j = 0; j < rows.length; ++j) {
      if (needNewLine) {
        formattedRows.append("\n");
      }
      final String row = rows[j];
      if (notTestUtilStack(row)) {
        final String formattedAssertation = formatAssertationCalls(row);
        final String formattedUserRow = formatUserRow(formattedAssertation,
            stackProps);
        formattedRows.append(formattedUserRow);
        needNewLine = true;
      } else {
        needNewLine = false;
      }
    }
    return formattedRows.toString();
  }

  private static String[] getStackByIndex(String[] stacks, int i) {
    String stack = stacks[i];
    return stack.split("\\n");
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
