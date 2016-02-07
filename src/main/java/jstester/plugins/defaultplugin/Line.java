package jstester.plugins.defaultplugin;

class Line {

  private static final String PROTOTYPE = "prototype";
  private static final String FUNCTION = "function";
  private static final String SKIP = "skip";
  private final String line;

  Line(String line) {
    this.line = line;
  }

  boolean isComment() {
    return line.trim().startsWith("//");
  }

  boolean isAFunctionDeclaration() {
    if (containsPrototypeDeclaration() && containsFunctionDeclaration()) {
      return true;
    }
    return false;
  }

  private boolean containsFunctionDeclaration() {
    String[] twoSideOfEquation = line.split("=");
    int length = twoSideOfEquation.length;
    return length == 2 && twoSideOfEquation[1].trim().startsWith(FUNCTION);
  }

  private boolean containsPrototypeDeclaration() {
    String[] dotSplit = line.trim().split("\\.");
    return dotSplit.length == 3 && dotSplit[1].equals(PROTOTYPE);
  }

  boolean containsSkip() {
    return line.contains(SKIP);
  }
}
