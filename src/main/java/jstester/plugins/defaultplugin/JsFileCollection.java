package jstester.plugins.defaultplugin;

import static jstester.JsTester.JS_TEST_UTIL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jstester.JsFile;

public class JsFileCollection implements Iterable<JsFile> {

  private static final String EVAL = "<eval>";

  private final JsFile[] jsFiles;

  /**
   * Initialize the collection of the given js-files.
   *
   * @param jsFiles
   *          sequence of user-codes.
   */
  public JsFileCollection(JsFile... jsFiles) {
    this.jsFiles = jsFiles;
  }

  /**
   * @return the size of this collection.
   */
  public int size() {
    return jsFiles.length;
  }

  /**
   * @return the whole content of this collection in {@link String}.
   */
  public String computeContent() {
    StringBuilder userCode = new StringBuilder();
    for (int i = 0; i < jsFiles.length; ++i) {
      if (i > 0) {
        userCode.append("\n");
      }
      userCode.append(jsFiles[i].toString());
    }
    return userCode.toString();
  }

  @Override
  public Iterator<JsFile> iterator() {
    return new Iterator<JsFile>() {

      private int i = 0;

      @Override
      public boolean hasNext() {
        return i < jsFiles.length;
      }

      @Override
      public JsFile next() {
        return jsFiles[i++];
      }
    };
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
        lineNum -= jsFiles[codeIndex].getLineNumbers() + 1;
        ++codeIndex;
      }
      final String line = partOne + ":" + lineNum + ")";
      return line.replace(EVAL, getJsFileName(codeIndex));
    } else {
      return row;
    }
  }

  private String getJsFileName(int codeIndex) {
    return jsFiles[codeIndex].getFileName() + ".js";
  }

  private boolean canDecraseLineNum(int lineNum, int codeIndex) {
    int lineNumbers = jsFiles[codeIndex].getLineNumbers();
    return lineNum - lineNumbers - 1 > 0;
  }

  /**
   * @param e
   *          exception
   * @param strictMode
   *          true, if we use the strict mode.
   * @return the formatted exception.
   */
  public String formatException(final Exception e, boolean strictMode) {
    String[] splittedMsg = e.getMessage().split("line number ");
    int lineNum = Integer.parseInt(splittedMsg[1]);
    if (strictMode) {
      lineNum -= 1;
    }
    int codeIndex = 0;
    while (lineNum - jsFiles[codeIndex].getLineNumbers() - 1 > 0) {
      lineNum -= jsFiles[codeIndex].getLineNumbers() + 1;
      ++codeIndex;
    }
    String actualFileName = jsFiles[codeIndex].getFileName();
    String firstPart = splittedMsg[0];
    String filenameMsg = firstPart.replace("<eval>", actualFileName);
    return filenameMsg + "line number " + lineNum + ".";
  }
}
