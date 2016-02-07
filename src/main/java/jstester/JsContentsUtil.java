package jstester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jstester.plugins.defaultplugin.JsFileCollection;

public final class JsContentsUtil {

  private JsContentsUtil() {
  }

  /**
   * Reading a js file from the classpath.
   *
   * @param path
   *          file's path
   * @return a {@link JsFile} which contains the js file's content
   * @throws IOException
   *           if an I/O error occurs
   */
  public static JsFile readFile(String path) {
    try {
      String fileName = "/" + path.replaceAll("\\.", "/") + ".js";
      InputStream is = JsTester.class.getResourceAsStream(fileName);
      InputStreamReader isReader = new InputStreamReader(is);
      BufferedReader reader = new BufferedReader(isReader);
      final StringBuilder sb = new StringBuilder();
      String line = reader.readLine();
      int lineNumber = 0;
      while (line != null) {
        sb.append(line + "\n");
        line = reader.readLine();
        ++lineNumber;
      }
      return new JsFile(path, lineNumber, sb.toString());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Reading a js files from the classpath.
   *
   * @param srcFiles
   *          files' paths
   * @return {@link JsFile} array which contains the js files' contents
   */
  public static JsFileCollection readFiles(String... srcFiles) {
    JsFile[] codes = new JsFile[srcFiles.length];
    for (int i = 0; i < srcFiles.length; ++i) {
      codes[i] = readFile(srcFiles[i]);
    }
    return new JsFileCollection(codes);
  }
}
