var SkipSomeTest = TestCase('SkipSomeTest');

SkipSomeTest.prototype.testAdd = function() {
  assertEquals(3, 1 + 2);
  assertEquals(7, 4 + 3);
};

// skip
SkipSomeTest.prototype.testAdd = function() {
  assertEquals(3, 2);
};

SkipSomeTest.prototype.testTimes = function() {
  assertEquals(12, 3 * 4);
};