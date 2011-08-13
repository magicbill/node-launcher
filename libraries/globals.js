/**
* Object GlobalScope
* @super Global
* @type constructor
* @memberOf GlobalScope
*/
GlobalScope.prototype = new Global();
function GlobalScope(){};

/**
 * Global scope reference.
 * N.B. eclipse does not know of attributes and methods of other modules. 
 */
GlobalScope.prototype.global = new Object();

/**
 * The global namespace object.
 * @type Global
 */
var global = new Object();

/**
 * The process object is a global object and can be accessed from anywhere. 
 * It is an instance of EventEmitter.
 */
GlobalScope.prototype.process = new EventEmitter();

/**
 * To require modules.
 * @param modulename
 * @constructor
 */
GlobalScope.prototype.require = function(modulename){};

// It seems for content assist functions can ont have attributes
// the following do not work
//GlobalScope.prototype.require.resolve = function(){};
//GlobalScope.prototype.require.cache = new Object();

/**
 * The filename of the script being executed. This is the absolute path, 
 * and not necessarily the same filename passed in as a command line argument.
 * Example: running node example.js from /Users/mjr
 */
GlobalScope.prototype.__filename = new String();

/**
 * The dirname of the script being executed.
 */
GlobalScope.prototype.__dirname = new String();


Module = function(){};

Module.prototype.exports = {};
/**
 * A reference to the current module. In particular module.exports is the same as the exports object. 
 * See src/node.js for more information. module isn't actually a global but rather local to each module.
 */
GlobalScope.prototype.module = new Module();

/**
 * An object which is shared between all instances of the current module and made accessible through require(). 
 * exports is the same as the module.exports object. 
 * See src/node.js for more information. exports isn't actually a global but rather local to each module.
 */
GlobalScope.prototype.exports = new Object();


/**
 * Browser-like object for printing to stdout and stderr.
 * property console
 * @type Console
 * @memberOf Console
 */
GlobalScope.prototype.console = new Console();


GlobalScope.prototype.setTimeout = function(cb, ms){};

GlobalScope.prototype.clearTimeout = function(t){};

GlobalScope.prototype.setInterval = function(cb, ms){};

GlobalScope.prototype.clearInterval = function(t){};


