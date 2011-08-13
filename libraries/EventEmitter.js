
/**
 * To access the EventEmitter class, require('events').EventEmitter.
 * 
 * @constructor
 */
var EventEmitter = new function(){};

EventEmitter.prototype.addListener = function(){};

/**
 * Adds a listener to the end of the listeners array for the specified event.
 */
EventEmitter.prototype.on = function(event, listener){};

/**
 * Adds a one time listener for the event. The listener is 
 * invoked only the first time the event is fired, after which it is removed.
 */
EventEmitter.prototype.once = function(event, listener){};

/**
 * Remove a listener from the listener array for the specified event. 
 * Caution: changes array indices in the listener array behind the listener.
 */
EventEmitter.prototype.removeListener = function(event, listener){};

/**
 * Removes all listeners, or those of the specified event.
 * @param event
 */
EventEmitter.prototype.removeAllListeners = function(event){};

/**
 * By default EventEmitters will print a warning if more than 
 * 10 listeners are added to it. 
 * This is a useful default which helps finding memory leaks. 
 * Obviously not all Emitters should be limited to 10. 
 * This function allows that to be increased. Set to zero for unlimited.
 */
EventEmitter.prototype.setMaxListeners = function(max){};

/**
 * Returns an array of listeners for the specified event. 
 * This array can be manipulated, e.g. to remove listeners.
 */
EventEmitter.prototype.listeners = function(event){};

/**
 * Execute each of the listeners in order with the supplied arguments.
 * @param event String
 * @param arg1 arguments
 */
EventEmitter.prototype.emit = function(event, arg1, arg2){};
