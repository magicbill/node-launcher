/**
 * Browser-like object for printing to stdout and stderr.
 * Accesed via the global property "console"
 * e.g. console.log("hello")
 * @type constructor
 */
function Console(){};

/**
* Prints to stdout with newline. This function can take multiple arguments in a printf()-like way.
* @param {Object} message
* @memberOf Console
*/
Console.prototype.log=function(message){};
/**
* Same as console.log.
* @param {Object} message
* @memberOf Console
*/
Console.prototype.info=function(message){};
/**
* function warn(message)
* @param {Object} message
* @memberOf Console
*/
Console.prototype.warn=function(message){};
/**
* function error(message)
* @param {Object} message
* @memberOf Console
*/
Console.prototype.error=function(message){};
/**
* Uses util.inspect on obj and prints resulting string to stderr.
* @param {Object} message
* @memberOf Console
*/
Console.prototype.dir=function(message){};
/**
* Mark a time.
* @param {Object} label
* @memberOf Console
*/
Console.prototype.time=function(label){};
/**
* Finish timer, record output.
* @param {Object} label
* @memberOf Console
*/
Console.prototype.timeEnd=function(label){};
/**
* Print a stack trace to stderr of the current position.
* @param {Object} message
* @memberOf Console
*/
Console.prototype.trace=function(message){};
/**
* Same as assert.ok().
* @param {Object} bool
* @memberOf Console
*/
Console.prototype.assert=function(bool){};
