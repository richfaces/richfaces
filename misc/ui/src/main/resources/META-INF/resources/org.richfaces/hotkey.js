if (!window.RichFaces) {
    window.RichFaces = {};
}
(function ($, rf) {

    rf.ui = rf.ui || {};
    var defaultOptions = {
    		type : 'keyup',
        	disableInInput : true
    };
    rf.ui.HotKey = function(componentId, options) {
        this.options = $.extend(this.options, defaultOptions, options);
        this.type = this.options.type;
        this.handler = this.options.handler;
        this.key = this.options.key;
        this.disableInInput = this.options.disableInInput;
        this.selector = this.options.selector;
        //TODO: improve to support just id as for other components.
        if (!this.selector){
        	jQuery(document).bind(this.type, this.options, rf.ui.HotKey.resolveHandler(this.handler));
        }else{
        	jQuery(this.selector).live(this.type, this.options, rf.ui.HotKey.resolveHandler(this.handler));
        }
    };
    rf.ui.HotKey.resolveHandler = function(handlerBody) {
    	if (handlerBody) {
    		if (typeof handlerBody == "function") {
    			return handlerBody;
    		} else {
    			return new Function("event", handlerBody);
    		}
    	}
    };
})(jQuery, RichFaces);