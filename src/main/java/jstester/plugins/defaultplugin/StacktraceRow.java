package jstester.plugins.defaultplugin;

final class StacktraceRow {

  private static final String EVAL = "<eval>";

  private String row;

  private StacktraceRow(String row) {
    this.row = row;
  }

  static StacktraceRow newStacktraceRowFrom(String row) {
    return new StacktraceRow(row);
  }

  boolean startsWithAtAssertion(String assertion) {
    return row.trim().startsWith("at " + assertion);
  }

  String withTheFileNameOfTestUtil() {
    return row.replace(EVAL, "test_util.js");
  }

  String asString() {
    return row;
  }

  boolean belongsToUserCodeStacktrace() {
    return doesntStartWithAtProgram() && doesntStartWithAtRunAllTests();
  }

  private boolean doesntStartWithAtProgram() {
    return !row.trim().startsWith("at <program>");
  }

  private boolean doesntStartWithAtRunAllTests() {
    return !row.trim().startsWith("at runAllTests");
  }
}
