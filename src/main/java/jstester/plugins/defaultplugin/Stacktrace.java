package jstester.plugins.defaultplugin;

import static jstester.plugins.defaultplugin.StacktraceRow.newStacktraceRowFrom;

import java.util.Iterator;

class Stacktrace implements Iterable<StacktraceRow> {

  private final String[] rows;

  Stacktrace(String[] rows) {
    this.rows = rows;
  }

  @Override
  public Iterator<StacktraceRow> iterator() {
    return new Iterator<StacktraceRow>() {

      private int i = 0;

      @Override
      public boolean hasNext() {
        return i < rows.length;
      }

      @Override
      public StacktraceRow next() {
        return newStacktraceRowFrom(rows[i++]);
      }
    };
  }
}
