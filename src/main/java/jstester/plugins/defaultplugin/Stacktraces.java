package jstester.plugins.defaultplugin;

import java.util.Iterator;

class Stacktraces implements Iterable<Stacktrace> {

  private final String[] stacktraces;

  Stacktraces(String stacktraces) {
    this.stacktraces = stacktraces.split("\\n\\n");
  }

  @Override
  public Iterator<Stacktrace> iterator() {
    return new Iterator<Stacktrace>() {

      private int i = 0;

      @Override
      public boolean hasNext() {
        return i < stacktraces.length;
      }

      @Override
      public Stacktrace next() {
        String[] rows = stacktraces[i++].split("\\n");
        return new Stacktrace(rows);
      }
    };
  }
}
