package jstester.plugins.defaultplugin;

import jstester.JsFile;

final class SkipFunctionUtil {

  private static final String SKIP_FUNCTION = "skipTestFunction";

  private SkipFunctionUtil() {
  }

  static String computeSkips(JsFileCollection userCodes) {
    StringBuilder skipTests = new StringBuilder();
    for (JsFile file : userCodes) {
      String content = file.toString();
      String[] lines = content.split("\\n");
      for (int i = 0; i < lines.length; ++i) {
        if (isSkipComment(lines, i)
            && new Line(lines[i + 1]).isAFunctionDeclaration()) {
          skipFunction(skipTests, lines[i + 1]);
        }
      }
    }
    return skipTests.toString();
  }

  private static boolean isSkipComment(String[] lines, int index) {
    Line line = new Line(lines[index]);
    boolean lastLine = index == (lines.length - 1);
    return !lastLine && line.isComment() && line.containsSkip();
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
}
