
/**
 * Pure Javascript is Unicode friendly but not nice to binary data. When dealing with TCP streams or the file system, it's necessary to handle octet streams. Node has several strategies for manipulating, creating, and consuming octet streams.
 * Raw data is stored in instances of the Buffer class. A Buffer is similar to an array of integers but corresponds to a raw memory allocation outside the V8 heap. A Buffer cannot be resized.
 * The Buffer object is global.
 * Converting between Buffers and JavaScript string objects requires an explicit encoding method. Here are the different string encodings;
 * <li>'ascii' - for 7 bit ASCII data only. This encoding method is very fast, and will strip the high bit if set. Note that this encoding converts a null character ('\0' or '\u0000') into 0x20 (character code of a space). If you want to convert a null character into 0x00, you should use 'utf8'.
 * <li>'utf8' - Multi byte encoded Unicode characters. Many web pages and other document formats use UTF-8.
 * <li>'ucs2' - 2-bytes, little endian encoded Unicode characters. It can encode only BMP(Basic Multilingual Plane, U+0000 - U+FFFF).
 * <li>'base64' - Base64 string encoding.
 * <li>'binary' - A way of encoding raw binary data into strings by using only the first 8 bits of each character. This encoding method is deprecated and should be avoided in favor of Buffer objects where possible. This encoding will be removed in future versions of Node.
 * <li>'hex' - Encode each byte as two hexidecimal characters.
 */

/**
 * @constructor
 */
var Buffer = new function(){};

Buffer.prototype.write = function(string, offset, length, encoding){};
Buffer.prototype.toString = function(encoding, start, end){};
// how buffer[index]
Buffer.prototype.isBuffer = function(obj){};
Buffer.prototype.byteLength = function(string, encoding){};
Buffer.prototype.copy = function(targetBuffer, targetStart, sourceStart, sourceEnd){};
Buffer.prototype.slice = function(start, end){};
Buffer.prototype.readUInt8 = function(offset, bigEndian){};
Buffer.prototype.readUInt16 = function(offset, bigEndian){};
Buffer.prototype.readUInt32 = function(offset, bigEndian){};
Buffer.prototype.readInt8 = function(offset, bigEndian){};
Buffer.prototype.readInt16 = function(offset, bigEndian){};
Buffer.prototype.readInt32 = function(offset, bigEndian){};
Buffer.prototype.readFloat = function(offset, bigEndian){};
Buffer.prototype.readDouble = function(offset, bigEndian){};
Buffer.prototype.writeUInt8 = function(value, offset, bigEndian){};
Buffer.prototype.writeUInt16 = function(value, offset, bigEndian){};
Buffer.prototype.writeUInt32 = function(value, offset, bigEndian){};
Buffer.prototype.writeInt8 = function(value, offset, bigEndian){};
Buffer.prototype.writeInt16 = function(value, offset, bigEndian){};
Buffer.prototype.writeInt32 = function(value, offset, bigEndian){};
Buffer.prototype.writeFloat = function(value, offset, bigEndian){};
Buffer.prototype.writeDouble = function(value, offset, bigEndian){};
Buffer.prototype.readUInt8NoChk = function(value, offset, bigEndian){};
Buffer.prototype.readUInt16NoChk = function(value, offset, bigEndian){};
Buffer.prototype.readUInt32NoChk = function(value, offset, bigEndian){};
Buffer.prototype.writeUInt8NoChk = function(value, offset, bigEndian){};
Buffer.prototype.writeUInt16NoChk = function(value, offset, bigEndian){};
Buffer.prototype.writeUInt32NoChk = function(value, offset, bigEndian){};
Buffer.prototype.fill = function(value, offset, length){};

Buffer.prototype.length = 0;
Buffer.prototype.INSPECT_MAX_BYTES = 50;
