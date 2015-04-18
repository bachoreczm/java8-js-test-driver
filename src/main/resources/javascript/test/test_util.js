var TestUtilTestObjects = {};
var SkippedTestFunctions = {};
var TestUtilErrors = [];

var TestCase = function(name) {
  var Obj = function() {};
  TestUtilTestObjects[name] = new Obj();
  return Obj;
};

var AssertationError = function(message) {
  this.name = 'AssertationError';
  this.message = message;
};

AssertationError.prototype = Error.prototype;

var assertTrue = function(condition) {
  if (!condition) {
    try {
      throw new AssertationError('');
    } catch(err) {
      TestUtilErrors.push(err.stack);
    }
  }
};

var assertFalse = function(condition) {
  if (condition) {
    try {
      throw new AssertationError();
    } catch(err) {
      TestUtilErrors.push(err.stack);
    }
  }
};

var assertNotNull = function(variable) {
  if (variable === null) {
    try {
      throw new AssertationError();
    } catch(err) {
      TestUtilErrors.push(err.stack);
    }
  }
};

var assertNull = function(variable) {
  if (variable !== null) {
    try {
      throw new AssertationError();
    } catch(err) {
      TestUtilErrors.push(err.stack);
    }
  }
};

var assertUndefined = function(variable) {
  if (variable !== undefined) {
    try {
      throw new AssertationError();
    } catch(err) {
      TestUtilErrors.push(err.stack);
    }
  }
};

var assertNotUndefined = function(variable) {
  if (variable === undefined) {
    try {
      throw new AssertationError();
    } catch(err) {
      TestUtilErrors.push(err.stack);
    }
  }
};

var assertArray = function(variable, msg) {
  if (Object.prototype.toString.call(variable) !== "[object Array]") {
    try {
      var errorMsg = '';
      if (msg !== undefined) {
        errorMsg = msg;
      }
      throw new AssertationError(errorMsg);
    } catch(err) {
      TestUtilErrors.push(err.stack);
    }
  }
};

var assertArrayEquals = function(expected, actual) {
  assertArray(expected, expected + " is not an array.");
  assertArray(actual, actual + " is not an array.");
  try {
    if (expected.length != actual.length) {
      throw new AssertationError('The two array\'s length are not the same');
    }
    var msg = 'Expected ' + expected + ', but was ' + actual;
    for (var i = 0; i < expected.length; ++i) {
      if (expected[i] != actual[i]) {
        throw new AssertationError(msg + ' (' + i + ').');
      }
    }
  } catch(err) {
	  TestUtilErrors.push(err.stack);
  }
};

var assertEquals = function(expected, actual) {
  if (expected != actual) {
    try {
      var msg = 'Expected ' + expected + ', but was ' + actual + '.';
      throw new AssertationError(msg);
    } catch(err) {
    	TestUtilErrors.push(err.stack);
    }
  }
};

var getMethods = function (obj) {
  var res = [];
  for(var m in obj) {
    if(typeof obj[m] == "function") {
      res.push(m);
    }
  }
  return res;
};

var skipTestFunction = function(testClassName, functionName) {
  SkippedTestFunctions[testClassName] = functionName;
}; 

var runAllTests = function() {
  for (instanceName in TestUtilTestObjects) {
    var testInstance = TestUtilTestObjects[instanceName];
    var methods = getMethods(testInstance)
    for (index in methods) {
      var method = methods[index];
	  if (method == undefined || isSkipMethod(method, instanceName)) {
        continue;
      }
	  try {
	    testInstance[method]();
	  } catch(err) {
        TestUtilErrors.push(err.stack);
	  }
    }
  }
};

var isSkipMethod = function(method, instanceName) {
  return (method + '') == SkippedTestFunctions[instanceName]
};

var JsTestDriverConsole = function() {
};

JsTestDriverConsole.prototype.log = function(message) {
	TestUtilErrors.push('JAVA8JSTDLOG:' + message + '\n');
};

var console = new JsTestDriverConsole();

var getTestErrors = function() {
  var errors = '';
  for (var i = 0; i < TestUtilErrors.length; ++i) {
    if (i > 0) {
      errors += '\n\n';
    }
    errors += TestUtilErrors[i];
  }
  return errors;
};
