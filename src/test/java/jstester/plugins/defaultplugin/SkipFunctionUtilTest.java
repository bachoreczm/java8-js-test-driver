package jstester.plugins.defaultplugin;

import static general.TestUtil.assertHasPrivateConstructor;
import static org.junit.Assert.assertEquals;
import jstester.JsFile;

import org.junit.Test;

public class SkipFunctionUtilTest {

  private static final String SKIP_FUNCTION = "skipTestFunction";

  @Test
  public void testPrivateConstructor() throws ReflectiveOperationException {
    assertHasPrivateConstructor(SkipFunctionUtil.class);
  }

  @Test
  public void testSkip() {
    String fileContent = getTestFileContent();
    JsFile props = new JsFile("file", 5, fileContent);
    String skipped = SkipFunctionUtil.computeSkips(props);
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
