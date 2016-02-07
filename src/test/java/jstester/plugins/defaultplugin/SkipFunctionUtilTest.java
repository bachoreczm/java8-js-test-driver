package jstester.plugins.defaultplugin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jstester.JsFile;

public class SkipFunctionUtilTest {

  private static final String SKIP_FUNCTION = "skipTestFunction";

  @Test
  public void testSkip() {
    String fileContent = getTestFileContent();
    JsFile props = new JsFile("file", 5, fileContent);
    String skipped = SkipFunctionUtil.computeSkips(new JsFileCollection(props));
    assertEquals(SKIP_FUNCTION + "('SkipTest', 'secondTest');\n", skipped);
  }

  private String getTestFileContent() {
    StringBuilder content = new StringBuilder();
    content.append("var SkipTest = new TestCase('SkipTest');\n");
    content.append("SkipTest.prototype.firstTest = function() {};\n");
    content.append("// skip\n");
    content.append("SkipTest.prototype.secondTest = function() {};\n");
    content.append("// skip\n");
    content.append("// other\n");
    content.append("SkipTest.prototype.thirdTest = function() {};\n");
    content.append("// skip\n");
    content.append("SkipTest.prototype.const = 4;\n");
    content.append("// skip\n");
    content.append("// SkipTest.fakeprototype.something");
    content.append("// skip\n");
    content.append("// SkipTest.prototype.fakeTest\n");
    content.append("// skip\n");
    String fileContent = content.toString();
    return fileContent;
  }
}
