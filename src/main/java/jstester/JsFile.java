package jstester;

public final class JsFile {

  private final String name;
  private final int lineNums;
  private final String content;

  /**
   * Sets final parameters.
   *
   * @param fileName
   *          the name of the file.
   * @param lineNumbers
   *          number of lines in the file.
   * @param fContent
   *          content of file.
   */
  public JsFile(String fileName, int lineNumbers, String fContent) {
    name = fileName;
    lineNums = lineNumbers;
    content = fContent;
  }

  /**
   * @return the name of the file.
   */
  public String getFileName() {
    return name;
  }

  /**
   * @return the number of lines in the file.
   */
  public int getLineNumbers() {
    return lineNums;
  }

  @Override
  public String toString() {
    return content;
  }
}
