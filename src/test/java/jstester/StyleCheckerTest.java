package jstester;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

public class StyleCheckerTest {

  @Test
  public void testSimpleStrictMode() throws IOException, ScriptException {
    String content1 = "var x = 0;\nvar y = 0;\n";
    JsFileProperties props1 = new JsFileProperties("file1", 2, content1);
    String content2 = "var z = 0;\nw = 0;\nbug(w);\n";
    JsFileProperties props2 = new JsFileProperties("file2", 3, content2);
    String content3 = "var z = 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaa';\n" + "\nvar a = 0;\n";
    JsFileProperties props3 = new JsFileProperties("file3", 2, content3);
    String content4 = "var z = 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaa';\n" + "\nvar a = 0;\n";
    JsFileProperties props4 = new JsFileProperties("file4", 2, content4);
    StyleChecker checker = new StyleChecker();
    checker.eval(new JsFileProperties[] {props1, props2, props3});
    String expected = "Line too long (file3:1).\n"
        + "ReferenceError: \"w\" is not defined in file2 at line number 2.\n";
    assertEquals(expected, checker.getStyleErrors());
    checker.eval(new JsFileProperties[] {props4});
    assertEquals("", checker.getStyleErrors());
  }

  @Test
  public void testDisableStrictMode() throws IOException, ScriptException {
    String content = "x = 0;\n";
    JsFileProperties props = new JsFileProperties("file", 1, content);
    StyleChecker checker = new StyleChecker();
    checker.disableStrictMode();
    checker.eval(new JsFileProperties[] {props});
    assertEquals("", checker.getStyleErrors());
    checker.enableStrictMode();
    checker.eval(new JsFileProperties[] {props});
    String expected = "ReferenceError: \"x\" is not defined in file "
        + "at line number 1.\n";
    assertEquals(expected, checker.getStyleErrors());
  }

  @Test
  public void testDisableStrictModeError() throws IOException, ScriptException {
    String content = "call()\n";
    JsFileProperties props = new JsFileProperties("file", 1, content);
    StyleChecker checker = new StyleChecker();
    checker.disableStrictMode();
    checker.eval(new JsFileProperties[] {props});
    String expected = "ReferenceError: \"call\" is not defined in file "
        + "at line number 1.\n";
    assertEquals(expected, checker.getStyleErrors());
  }

  @Test
  public void testTypeError() throws IOException, ScriptException {
    String content = "null.f()\n";
    JsFileProperties props = new JsFileProperties("file", 1, content);
    StyleChecker checker = new StyleChecker();
    checker.disableStrictMode();
    checker.eval(new JsFileProperties[] {props});
    String expected = "TypeError: null has no such function \"f\" in file "
        + "at line number 1.\n";
    assertEquals(expected, checker.getStyleErrors());
  }
}
