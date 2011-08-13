var util = require('util');

/**
 * 
 * @constructor
 */
var MyModule = function() {
	this.attr = "Hello World";
};

MyModule.prototype.operation = function() {
	console.log(this.attr);
};


module.exports.MyModule = MyModule;
