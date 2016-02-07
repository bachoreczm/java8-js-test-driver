package jstester.plugins.stylechecker;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import jstester.JsFile;
import jstester.plugins.defaultplugin.JsFileCollection;

public class CurlyBracesCheckerTest {

  private StyleRule curlyChecker;

  @Before
  public void setUp() {
    curlyChecker = new CurlyBracesChecker();
  }

  @Test
  public void testIf() {
    StringBuilder fileBuilder = new StringBuilder();
    fileBuilder.append("if (true) {}\n");
    fileBuilder.append("if (true)\nvar a = 2;\n");
    String content = fileBuilder.toString();
    String fileName = "curly_braces.js";
    String expectedMsg = "Missing curly brace (curly_braces.js:2).\n";
    JsFile file = new JsFile(fileName, 2, content);
    JsFile[] userCodes = new JsFile[] {file};
    String actualMsg = curlyChecker.checkRule(new JsFileCollection(userCodes));
    assertEquals(expectedMsg, actualMsg);
  }

  @Test
  public void testElse() {
    StringBuilder fileBuilder = new StringBuilder();
    fileBuilder.append("if (true) {}\n");
    fileBuilder.append("if (true) {\nvar a = 2;} else\nvar c = 3;\n");
    String content = fileBuilder.toString();
    String fileName = "curly_braces.js";
    String expectedMsg = "Missing curly brace (curly_braces.js:3).\n";
    JsFile file = new JsFile(fileName, 2, content);
    JsFile[] userCodes = new JsFile[] {file};
    String actualMsg = curlyChecker.checkRule(new JsFileCollection(userCodes));
    assertEquals(expectedMsg, actualMsg);
  }

  @Test
  public void testIfInFunction() {
    StringBuilder fileBuilder = new StringBuilder();
    fileBuilder.append("var func = function() {\n");
    fileBuilder.append("while(true) {break;}\n");
    fileBuilder.append("if (true)\nvar a = 2;\n");
    fileBuilder.append("}");
    String content = fileBuilder.toString();
    String fileName = "curly_braces.js";
    String expectedMsg = "Missing curly brace (curly_braces.js:3).\n";
    JsFile file = new JsFile(fileName, 2, content);
    JsFile[] userCodes = new JsFile[] {file};
    String actualMsg = curlyChecker.checkRule(new JsFileCollection(userCodes));
    assertEquals(expectedMsg, actualMsg);
  }

  @Test
  public void testForWithTwoFilesFirstCorrectSecondIncorrect() {
    String okContent = "var ok = 'ok';";
    JsFile fileOk = new JsFile("ok", 1, okContent);
    String fileContent = "var a = 0;\nfor (var i = 0; i < 3; ++i)\nvar a = 2;";
    String fileName = "curly_braces.js";
    JsFile file = new JsFile(fileName, 1, fileContent);
    String expectedMsg = "Missing curly brace (curly_braces.js:2).\n";
    JsFile[] userCodes = new JsFile[] {fileOk, file};
    String actualMsg = curlyChecker.checkRule(new JsFileCollection(userCodes));
    assertEquals(expectedMsg, actualMsg);
  }

  @Test
  public void testWithEmptyFile() {
    JsFile file = new JsFile("name", 0, "\n");
    JsFile[] userCodes = new JsFile[] {file};
    String actualMsg = curlyChecker.checkRule(new JsFileCollection(userCodes));
    assertEquals("", actualMsg);
  }
}
