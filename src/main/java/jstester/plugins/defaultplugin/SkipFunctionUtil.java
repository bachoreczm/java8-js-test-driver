package jstester.plugins.defaultplugin;

import jstester.JsFile;

final class SkipFunctionUtil {

  private static final String SKIP_FUNCTION = "skipTestFunction";
  private static final String PROTOTYPE = "prototype";
  private static final String FUNCTION = "function";
  private static final String SKIP = "skip";

  private SkipFunctionUtil() {
  }

  static String computeSkips(JsFile... userCodes) {
    StringBuilder skipTests = new StringBuilder();
    for (JsFile file : userCodes) {
      String content = file.toString();
      String[] lines = content.split("\\n");
      for (int i = 0; i < lines.length; ++i) {
        if (isSkipComment(lines, i) && isFunction(lines[i + 1])) {
          skipFunction(skipTests, lines[i + 1]);
        }
      }
    }
    return skipTests.toString();
  }

  private static boolean isSkipComment(String[] lines, int index) {
    String line = lines[index];
    boolean lastLine = index == (lines.length - 1);
    return !lastLine && isComment(line) && line.contains(SKIP);
  }

  private static boolean isFunction(final String line) {
    String[] dotSplit = line.trim().split("\\.");
    String[] equalSplit = line.split("=");
    if (prototypeLine(dotSplit) && functionLine(equalSplit)) {
      return true;
    }
    return false;
  }

  private static void skipFunction(StringBuilder skipTests, String line) {
    String[] dotSplit = line.trim().split("\\.");
    String testClass = "'" + dotSplit[0] + "'";
    String testFunc = "'" + dotSplit[2].split("=")[0].trim() + "'";
    String jsCode = callSkipFunction(testClass, testFunc);
    skipTests.append(jsCode);
  }

  private static String callSkipFunction(String testClass, String testFunc) {
    return SKIP_FUNCTION + "(" + testClass + ", " + testFunc + ");\n";
  }

  private static boolean prototypeLine(String[] dotSplit) {
    return dotSplit.length == 3 && dotSplit[1].equals(PROTOTYPE);
  }

  private static boolean functionLine(String[] equalSplit) {
    int length = equalSplit.length;
    return length == 2 && equalSplit[1].trim().startsWith(FUNCTION);
  }

  private static boolean isComment(String line) {
    return line.trim().startsWith("//");
  }
}
