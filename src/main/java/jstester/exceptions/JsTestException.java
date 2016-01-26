package jstester.exceptions;

public class JsTestException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * @param msg
   *          the exception message
   */
  public JsTestException(String msg) {
    super(msg);
  }

  /**
   * @param ex
   *          packaged exception
   */
  public JsTestException(Exception ex) {
    super(ex);
  }
}
