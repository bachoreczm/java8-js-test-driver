var AssertsTest = TestCase('AssertsTest');

AssertsTest.prototype.testAssertions = function() {
  assertEquals(4, 4);
  assertArray([1, 2, 3]);
  var nullVariable = null;
  assertNull(nullVariable);
  assertNotNull("not null");
  assertArrayEquals([1, 2, 3], [1, 2, 3]);
  assertUndefined(undefined);
  assertNotUndefined(3);
  assertTrue(3 == 3);
  assertFalse(3 == 4);
};