package jstester.plugins.defaultplugin;

import java.util.Iterator;

class Stacktrace implements Iterable<String> {

  private final String[] rows;

  Stacktrace(String[] rows) {
    this.rows = rows;
  }

  @Override
  public Iterator<String> iterator() {
    return new Iterator<String>() {

      private int i = 0;

      @Override
      public boolean hasNext() {
        return i < rows.length;
      }

      @Override
      public String next() {
        return rows[i++];
      }
    };
  }
}
