# java8-jstd
Javascript tester in java8 (you <b>DON'T NEED a BROWSER</b>).

This is basically compatible with the js-test-driver project.

##Usage

Call the 'runTestsAndGetErrors()' function of the 'JsTester' class (jstester package).

Your js files must be on the classpath (e.g. in the resources folder).

###Example test_js.js file (e.g.: src/test/resources/javascript/test.js)
<pre>
var ExampleTest = TestCase('ExampleTest');

ExampleTest.prototype.test = function() {
  assertEquals(3, 3);
};
</pre>

###Example SimpleTest.java file

<pre>
import static jstester.JsTester.runTestsAndGetErrors;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

public class SimpleTest {

  @Test
  public void runJsTests() throws IOException, ScriptException {
    String testFile = "javascript.test_js";
    assertTrue(runTestsAndGetErrors(testFile).equals(""));
  }
}
</pre>

You can call runTestsAndGetErrors with your srcFiles (no matter how much, it can be 0, or 1, 2, 3, ...).
<code>runTestsAndGetErrors(testFile, srcFile1, srcFile2, ...)</code>

##Assertations
###assertTrue(condition)
Fails, if the condition is false.
###assertFalse(condition)
Fails, if the condition is true.
###assertEquals(expected, actual)
Fails, if the actual variable is not equal to the expected.
###assertNotEquals(expected, actual)
Fails, if the actual variable is equal to the expected.
###assertSame(expected, actual)
Fails, if the actual variable is not same as the expected.
###assertNotSame(expected, actual)
Fails, if the actual variable is not same as the expected.
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
###assertTypeOf(expected, value)
Fails, if the type of the given value is not the expected string.
###assertBoolean(variable)
Fails, if the type of the given variable is not boolean.
###assertFunction(variable)
Fails, if the type of the given variable is not function.
###assertObject(variable)
Fails, if the type of the given variable is not object.
###assertNumber(variable)
Fails, if the type of the given variable is not number.
###assertString(variable)
Fails, if the type of the given variable is not string.

##See also

https://github.com/eriwen/gradle-js-plugin
