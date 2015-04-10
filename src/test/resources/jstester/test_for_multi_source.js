var CallerTest = TestCase('CallerTest');

CallerTest.prototype.testCallBug = function() {
  var caller = new Caller();
  caller.callBug();
};