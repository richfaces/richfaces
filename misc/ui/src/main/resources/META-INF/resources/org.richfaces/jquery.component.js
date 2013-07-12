if (!window.RichFaces) {
    window.RichFaces = {};
}

(function(jquery, richfaces) {

    var evaluate = function(selector) {
        var result = selector;
        try {
            result = eval(selector);
        } catch (e) {
            //do nothing
        }
        return result;
    };

    var evaluateJQuery = function(element, selector) {
        var result = element || evaluate(selector);
        if (!(result instanceof jquery)) {
            result = jquery(result || "");
        }

        return result;
    };

    var createEventHandlerFunction = function(opts) {
        return function() {
            var selector = evaluateJQuery(null, opts.selector);
            selector[opts.attachType || "bind"](opts.event, null, new Function("event", opts.query));
        };
    };

    var createDirectQueryFunction = function(opts) {
        var queryFunction = new Function("options", "arguments[1]." + opts.query);

        return function() {
            var element;
            var options;

            if (arguments.length == 1) {             
    	        if (!opts.selector) {
		        	//if selector is not present take selector as default
		         	element = arguments[0];
	        	}else{
		        	//if selector is allready presen from rich jQuery component take options as default.
			        options = arguments[0];	
	        	}		     
		
            } else {
                //function(element, options) { ...query()... }
                element = arguments[0];
                options = arguments[1];
            }

            var selector = evaluateJQuery(element, opts.selector);
            queryFunction.call(this, options, selector);
        };
    };

    var createQueryFunction = function(options) {
        if (options.event) {
            return createEventHandlerFunction(options);
        } else {
            return createDirectQueryFunction(options);
        }
    };

    var query = function(options) {
        if (options.timing == 'immediate') {
            createQueryFunction(options).call(this);
        } else {
            jquery(document).ready(createQueryFunction(options));
        }
    };

    richfaces.jQuery = {

        createFunction: createQueryFunction,

        query: query

    };

}(jQuery, RichFaces));
