package jstester.stylechecker;

import static org.junit.Assert.*;
import jstester.JsFileProperties;

import org.junit.Before;
import org.junit.Test;

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
    JsFileProperties file = new JsFileProperties(fileName, 2, content);
    JsFileProperties[] userCodes = new JsFileProperties[] {file};
    String actualMsg = curlyChecker.checkRule(userCodes);
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
    JsFileProperties file = new JsFileProperties(fileName, 2, content);
    JsFileProperties[] userCodes = new JsFileProperties[] {file};
    String actualMsg = curlyChecker.checkRule(userCodes);
    assertEquals(expectedMsg, actualMsg);
  }

  @Test
  public void testForWithTwoFilesFirstCorrectSecondIncorrect() {
    String okContent = "var ok = 'ok';";
    JsFileProperties fileOk = new JsFileProperties("ok", 1, okContent);
    String fileContent = "var a = 0;\nfor (var i = 0; i < 3; ++i)\nvar a = 2;";
    String fileName = "curly_braces.js";
    JsFileProperties file = new JsFileProperties(fileName, 1, fileContent);
    String expectedMsg = "Missing curly brace (curly_braces.js:2).\n";
    JsFileProperties[] userCodes = new JsFileProperties[] {fileOk, file};
    String actualMsg = curlyChecker.checkRule(userCodes);
    assertEquals(expectedMsg, actualMsg);
  }

  @Test
  public void testWithEmptyFile() {
    JsFileProperties file = new JsFileProperties("name", 0, "\n");
    JsFileProperties[] userCodes = new JsFileProperties[] {file};
    String actualMsg = curlyChecker.checkRule(userCodes);
    assertEquals("", actualMsg);
  }
}
