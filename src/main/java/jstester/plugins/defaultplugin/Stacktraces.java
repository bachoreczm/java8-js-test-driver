package jstester.plugins.defaultplugin;

class Stacktraces {

  private final String[] stacktraces;

  Stacktraces(String stacktraces) {
    this.stacktraces = stacktraces.split("\\n\\n");
  }

  String formatTraceRows(JsCodeBase jsCodeBase) {
    StringBuilder formattedRows = new StringBuilder();
    for (int i = 0; i < stacktraces.length; ++i) {
      if (i > 0) {
        formattedRows.append("\n");
      }
      Stacktrace currentStacktrace = get(i);
      formattedRows.append(formatOneStackTrace(jsCodeBase, currentStacktrace));
    }
    return formattedRows.toString();
  }

  private static String formatOneStackTrace(JsCodeBase jsCodeBase,
      Stacktrace stacktrace) {
    boolean needNewLine = false;
    StringBuilder formattedRows = new StringBuilder();
    for (String row : stacktrace) {
      if (needNewLine) {
        formattedRows.append("\n");
      }
      if (notTestUtilStack(row)) {
        String formattedRow = jsCodeBase.formatStacktraceRow(row);
        formattedRows.append(formattedRow);
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

  private Stacktrace get(int i) {
    return new Stacktrace(stacktraces[i].split("\\n"));
  }
}
