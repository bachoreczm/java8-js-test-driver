var Child = function() {
};

Child.prototype.getAge = function() {
  return 3;
};

Child.prototype.isBoy = function() {
  return true;
};

Child.prototype.isGirl = function() {
  return false;
};

Child.prototype.bug = function() {
  return this.errorCall();
};