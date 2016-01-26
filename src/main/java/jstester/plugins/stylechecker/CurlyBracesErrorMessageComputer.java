package jstester.plugins.stylechecker;

import java.util.List;

import jstester.JsFileProperties;

class CurlyBracesErrorMessageComputer {

  private static final String LINE_SEPARATOR = "\n";

  String computeErrorMessage(JsFileProperties file, List<Integer> positions) {
    String[] contentRows = computeSplittedRows(file);
    StringBuilder messages = new StringBuilder();
    String name = file.getFileName();
    int cnt = 0;
    int currentIndex = 0;
    for (int i = 0; i < contentRows.length; ++i) {
      if (thisIsTheEnd(positions, currentIndex)) {
        break;
      }
      cnt += contentRows[i].length() + 1;
      if (cnt >= positions.get(currentIndex)) {
        messages.append(errorMessage(name, i + 1));
        ++currentIndex;
      }
    }
    return messages.toString();
  }

  private boolean thisIsTheEnd(List<Integer> positions, int currentIndex) {
    return currentIndex == positions.size();
  }

  private String[] computeSplittedRows(JsFileProperties file) {
    String content = file.toString();
    return content.split(LINE_SEPARATOR);
  }

  private String errorMessage(String fileName, int lineNumber) {
    return "Missing curly brace (" + fileName + ":" + lineNumber + ").\n";
  }
}
