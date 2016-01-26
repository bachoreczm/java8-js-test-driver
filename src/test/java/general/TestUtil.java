package general;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public final class TestUtil {

  private TestUtil() {
  }

  /**
   * Checker for the given {@link Class} private constructor.
   *
   * @param clazz
   *          the specified {@link Class}.
   * @param <T>
   *          clazz type
   * @throws ReflectiveOperationException
   *           if an error occurs, when getting the constructor
   * @throws AssertionError
   *           if the given {@link Class} has no private constructor
   */
  public static <T> void assertHasPrivateConstructor(Class<T> clazz)
      throws ReflectiveOperationException {
    String name = clazz.getName();
    Constructor<T> constructor = clazz.getDeclaredConstructor();
    if (!Modifier.isPrivate(constructor.getModifiers())) {
      String message = name + " has no private construcor.";
      throw new AssertionError(message);
    }
    constructor.setAccessible(true);
    constructor.newInstance();
  }
}
