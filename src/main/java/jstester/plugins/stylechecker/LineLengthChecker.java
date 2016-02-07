package jstester.plugins.stylechecker;

import jstester.JsFile;
import jstester.plugins.defaultplugin.JsFileCollection;

class LineLengthChecker implements StyleRule {

  private int lineLength;

  LineLengthChecker(int maxLineLength) {
    lineLength = maxLineLength;
  }

  @Override
  public String checkRule(JsFileCollection userCodes) {
    StringBuilder styleErrors = new StringBuilder();
    for (JsFile jsFile : userCodes) {
      String[] actualFileRows = jsFile.toString().split("\n");
      String actualFile = jsFile.getFileName();
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
