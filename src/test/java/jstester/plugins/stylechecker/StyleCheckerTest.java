package jstester.plugins.stylechecker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jstester.JsFile;
import jstester.plugins.defaultplugin.JsFileCollection;
import jstester.plugins.stylechecker.StyleChecker.StyleError;

public class StyleCheckerTest {

  @Test
  public void testSimpleStrictMode() {
    String content1 = "var x = 0;\nvar y = 0;\n";
    JsFile props1 = new JsFile("file1", 2, content1);
    String content2 = "var z = 0;\nw = 0;\nbug(w);\n";
    JsFile props2 = new JsFile("file2", 3, content2);
    String content3 = "var z = 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaa';\n" + "\nvar a = 0;\n";
    JsFile props3 = new JsFile("file3", 2, content3);
    StyleChecker checker = new StyleChecker(
        new JsFileCollection(props1, props2, props3));
    checker.eval();
    String expected = "Line too long (file3:1).\n"
        + "ReferenceError: \"w\" is not defined in file2 at line number 2.\n";
    assertEquals(expected, catchErrors(checker));
  }

  @Test
  public void noStyleErrorsWhenUsingVar() {
    String content = "var z = 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaa';\n" + "\nvar a = 0;\n";
    JsFile props = new JsFile("file4", 2, content);
    StyleChecker checker = new StyleChecker(new JsFileCollection(props));
    checker.eval();
    assertEquals("", catchErrors(checker));
  }

  @Test
  public void testDisableStrictMode() {
    String content = "x = 0;\n";
    JsFile props = new JsFile("file", 1, content);
    StyleChecker checker = new StyleChecker(new JsFileCollection(props));
    checker.disableStrictMode();
    checker.eval();
    checker.assertForNoStyleErrors();
    checker.enableStrictMode();
    checker.eval();
    String expected = "ReferenceError: \"x\" is not defined in file "
        + "at line number 1.\n";
    assertEquals(expected, catchErrors(checker));
  }

  @Test
  public void testDisableStrictModeError() {
    String content = "call()\n";
    JsFile props = new JsFile("file", 1, content);
    StyleChecker checker = new StyleChecker(new JsFileCollection(props));
    checker.disableStrictMode();
    checker.eval();
    String expected = "ReferenceError: \"call\" is not defined in file "
        + "at line number 1.\n";
    assertEquals(expected, catchErrors(checker));
  }

  @Test
  public void testTypeError() {
    String content = "null.f()\n";
    JsFile props = new JsFile("file", 1, content);
    StyleChecker checker = new StyleChecker(new JsFileCollection(props));
    checker.disableStrictMode();
    checker.eval();
    String expected = "TypeError: null has no such function \"f\" in file "
        + "at line number 1.\n";
    assertEquals(expected, catchErrors(checker));
  }

  private String catchErrors(StyleChecker checker) {
    try {
      checker.assertForNoStyleErrors();
      return "";
    } catch (StyleError exception) {
      return exception.getMessage();
    }
  }
}
