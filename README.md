# java8-jstd
Javascript tester in java8 (you <b>DON'T NEED BROWSER</b>).

This is basically compatible with js-test-driver project.

##Usage

Call the 'JsTester' (jstester package) class 'runtestsAndGetErrors' function.

Your js files must be on classpath (e.g. in the resources folder).

###Example test_js.js file (e.g.: src/test/resources/javascript/test.js)
<pre>
var ExampleTest = TestCase('ExampleTest');

ExampleTest.prototype.test = function() {
  assertEquals(3, 3);
};
</pre>

###Example SimpleTest.java file

<pre>
import static jstester.JsTester.runtestsAndGetErrors;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

public class SimpleTest {

  @Test
  public void runJsTests() throws IOException, ScriptException {
    String testFile = "javascript.test_js";
    assertTrue(runtestsAndGetErrors(testFile).equals(""));
  }
}
</pre>

You can call runtestsAndGetErrors with your srcFiles (no matter how much, it can be 0, or 1, 2, 3, ...).
<code>runtestsAndGetErrors(testFile, srcFile1, srcFile2, ...)</code>

##Assertations
###assertTrue(condition)
Fails, if the condition is false.
###assertFalse(condition)
Fails, if the condition is true.
###assertEquals(expected, actual)
Fails, if the actual variable is not equal to the expected.
###assertNull(variable)
Fails, if the given variable is null.
###assertNotNull(variable)
Fails, if the given variable is not null.
###assertUndefined(variable)
Fails, if the given variable is undefined.
###assertNotUndefined(variable)
Fails, if the given variable is not undefined.
###assertArray(variable)
Fails, if the given variable is an array.
###assertArrayEquals(expected, actual)
Fails, if the given variables are arrays and there are same elements in the arrays.

##See also

https://github.com/eriwen/gradle-js-plugin
