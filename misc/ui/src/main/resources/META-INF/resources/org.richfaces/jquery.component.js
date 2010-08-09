if (!window.RichFaces) {
	window.RichFaces = {};
}

(function(jquery, richfaces) {

	var evaluateSelector = function(selector) {
		var result = selector;
		try {
			result = eval(selector);
		} catch (e) {
			//do nothing
		}
		return result || "";
	};

	var createHandler = function(handler) {
		if (typeof handler == 'function') {
			return handler;
		} else {
			return new Function("event", handler);
		}
	};

	var bindFunction = function(options) {

		var selector = evaluateSelector(options.selector);
		var query = options.query;

		if (options.event) {
			var handler = createHandler(query);

			if (options.attachType == 'live') {
				jQuery(selector).live(options.event, handler);
			} else if (options.attachType == 'one') {
				jQuery(selector).one(options.event, handler);
			} else {
				jQuery(selector).bind(options.event, handler);
			}
		} else {
			var f = new Function("__locatedObject", "__locatedObject." + query);
			f.call(this, jQuery(selector));
			//TODO return value?
		}
	};

	var bind = function(options) {
		if (options.timing == 'immediate') {
			bindFunction(options);
		} else {
			jQuery(document).ready(function() {
				bindFunction(options);
			});
		}
	};

	richfaces.jQuery = {

		bind: bind,

		createBinder: function(options) {
			return function() {
				bind(options);
			};
		}
	};

}(jQuery, RichFaces));
