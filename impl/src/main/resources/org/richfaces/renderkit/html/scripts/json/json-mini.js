/*
    json.js
    2006-12-06

    This file adds these methods to JavaScript:

        array.toJSONString()
        boolean.toJSONString()
        date.toJSONString()
        number.toJSONString()
        object.toJSONString()
        string.toJSONString()
            These methods produce a JSON text from a JavaScript value.
            It must not contain any cyclical references. Illegal values
            will be excluded.

            The default conversion for dates is to an ISO string. You can
            add a toJSONString method to any date object to get a different
            representation.

        string.parseJSON(hook)
            This method parses a JSON text to produce an object or
            array. It can throw a SyntaxError exception.

            The optional hook parameter is a function which can filter and
            transform the results. It receives each of the values, and its
            return value is used instead. If it returns what it received, then
            structure is not modified.

            Example:

            // Parse the text. If it contains any "NaN" strings, replace them
            // with the NaN value. All other values are left alone.

            myData = text.parseJSON(function (value) {
                if (value === 'NaN') {
                    return NaN;
                }
                return value;
            });

    It is expected that these methods will formally become part of the
    JavaScript Programming Language in the Fourth Edition of the
    ECMAScript standard in 2007.
*/

if (!String.prototype.parseJSON) {
    String.prototype.parseJSON = function (hook) {
        try {
            if (!/[^,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]/.test(this.replace(/("(\\.|[^"\\])*")|('(\\.|[^'\\])*')/g, ''))) {
                var j = eval('(' + this + ')');
                if (typeof hook === 'function') {
                    function walk(v) {
                        if (v && typeof v === 'object') {
                            for (var i in v) {
                                if (v.hasOwnProperty(i)) {
                                    v[i] = walk(v[i]);
                                }
                            }
                        }
                        return hook(v);
                    }
                    return walk(j);
                }
                return j;
            }
        } catch (e) {
        }
        throw new SyntaxError("parseJSON");
    };
}

EventHandlersWalk = function(v) {
    if (v && typeof v == 'object') {
	    var names = new Array();
	    for (var i in v) {
	        if (v.hasOwnProperty(i)) {
	            if (i.length > 2 && i.substring(0, 2) == 'on') {
	            	names.push(i);
	            }
	        }
	    }
	    
	    for (var i = 0; i < names.length; i++) {
	    	var name = names[i];
		    var value = v[name];
		    if (value && typeof value != 'function') {
			    var f = eval('([' + v[name] + '])')[0];
			    if (typeof f == 'function') {
			    	v[name] = f;
			    }
		    }
	    }
    }

	return v;    
};