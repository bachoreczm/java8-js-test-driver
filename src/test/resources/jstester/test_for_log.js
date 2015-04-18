var LogTest = TestCase('LogTest');

LogTest.prototype.testLog = function() {
  assertEquals(3, 3);
  console.log('this is a log message...');
  assertTrue(4 === 4);
};