var StackTraceTests = TestCase('StackTraceTests');

StackTraceTests.prototype.test1 = function() {
  assertEquals(3, 4);
};

StackTraceTests.prototype.test2 = function() {
  assertEquals(5, 4);
};

StackTraceTests.prototype.test3 = function() {
  var child = new Child();
  assertEquals(3, child.getAge());
  assertTrue(child.isBoy());
  assertTrue(child.somefunc.isGirl());
};

StackTraceTests.prototype.test4 = function() {
  var child = new Child();
  child.bug();
}