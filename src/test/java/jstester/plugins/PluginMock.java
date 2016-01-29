package jstester.plugins;

import java.io.IOException;

import jstester.JsFile;

class PluginMock implements JsTestPlugin {

  private int called = 0;

  @Override
  public void eval(JsFile[] userCodes) throws IOException {
    ++called;
  }

  public int getCalled() {
    return called;
  }

  @Override
  public String getName() {
    return "Mockplugin";
  }

  @Override
  public String getLastRunResults() {
    return "results";
  }
}
