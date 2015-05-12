package jstester.stylechecker;

import jstester.JsFileProperties;

class LineLengthChecker implements StyleRule {

  private int lineLength;

  LineLengthChecker(int maxLineLength) {
    lineLength = maxLineLength;
  }

  @Override
  public String checkRule(JsFileProperties[] userCodes) {
    StringBuilder styleErrors = new StringBuilder();
    for (int i = 0; i < userCodes.length; ++i) {
      String[] actualFileRows = userCodes[i].toString().split("\n");
      String actualFile = userCodes[i].getFileName();
      for (int j = 0; j < actualFileRows.length; ++j) {
        if (actualFileRows[j].length() > lineLength) {
          String err = "Line too long (" + actualFile + ":" + (j + 1) + ").\n";
          styleErrors.append(err);
        }
      }
    }
    return styleErrors.toString();
  }
}
