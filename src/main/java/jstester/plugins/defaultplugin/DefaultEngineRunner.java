package jstester.plugins.defaultplugin;

import static jstester.JsTester.JS_TEST_UTIL;
import static jstester.JsTester.newEngine;

import javax.script.ScriptException;

import jstester.exceptions.JsTestException;

class DefaultEngineRunner {

  private static final String RUNCOMMAND = "runAllTests();\ngetTestErrors();";

  private final JsFileCollection userCodes;

  DefaultEngineRunner(JsFileCollection userCodes) {
    this.userCodes = userCodes;
  }

  String run() {
    try {
      String rawJsCode = computeRawJsCode();
      return (String) newEngine().eval(rawJsCode);
    } catch (ScriptException e) {
      throw new JsTestException(e);
    }
  }

  private String computeRawJsCode() {
    String rawUserCode = userCodes.computeContent();
    String skipTests = SkipFunctionUtil.computeSkips(userCodes);
    String runnerCode = skipTests + RUNCOMMAND;
    return JS_TEST_UTIL + "\n" + rawUserCode + "\n" + runnerCode;
  }
}
