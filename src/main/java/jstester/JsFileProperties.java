package jstester;

final class JsFileProperties {

  private final String name;
  private final int lineNums;
  private final String content;

  JsFileProperties(String fileName, int lineNumbers, String fileContent) {
    name = fileName;
    lineNums = lineNumbers;
    content = fileContent;
  }

  String getFileName() {
    return name;
  }

  int getLineNumbers() {
    return lineNums;
  }

  @Override
  public String toString() {
    return content;
  }
}
