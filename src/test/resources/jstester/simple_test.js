var ChildTest = TestCase('ChildTest');

ChildTest.prototype.test = function() {
  var child = new Child();
  assertEquals(3, child.getAge());
  assertTrue(child.isBoy());
  assertFalse(child.isGirl());
};