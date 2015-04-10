var Caller = function() {
};

Caller.prototype.callBug = function() {
  var child = new Child();
  return child.bug();
};