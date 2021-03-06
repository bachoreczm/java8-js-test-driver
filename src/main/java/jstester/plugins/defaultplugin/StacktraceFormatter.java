package jstester.plugins.defaultplugin;

class StacktraceFormatter {

  private final String[] stacktraces;

  StacktraceFormatter(String stacktraces) {
    this.stacktraces = stacktraces.split("\\n\\n");
  }

  String formatStacktraceRows(JsFileCollection userCodes) {
    StringBuilder formattedRows = new StringBuilder();
    for (int i = 0; i < stacktraces.length; ++i) {
      if (i > 0) {
        formattedRows.append("\n");
      }
      Stacktrace currentStacktrace = getStacktraceByIndex(i);
      formattedRows
          .append(formatOneStackTrace(userCodes, currentStacktrace));
    }
    return formattedRows.toString();
  }

  private static String formatOneStackTrace(JsFileCollection userCodes,
      Stacktrace stacktrace) {
    boolean needNewLine = false;
    StringBuilder formattedRows = new StringBuilder();
    for (StacktraceRow stacktraceRow : stacktrace) {
      if (needNewLine) {
        formattedRows.append("\n");
      }
      if (stacktraceRow.belongsToUserCodeStacktrace()) {
        String formattedRow = userCodes.formatStacktraceRow(stacktraceRow);
        formattedRows.append(formattedRow);
        needNewLine = true;
      } else {
        needNewLine = false;
      }
    }
    return formattedRows.toString();
  }

  private Stacktrace getStacktraceByIndex(int i) {
    return new Stacktrace(stacktraces[i].split("\\n"));
  }
}
