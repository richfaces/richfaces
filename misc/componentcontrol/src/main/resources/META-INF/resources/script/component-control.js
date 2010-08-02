(function ($, richfaces) {
    
    richfaces.ui = richfaces.ui || {};
    
    richfaces.ui.ComponentControl = richfaces.ui.ComponentControl || {};
    
    $.extend(richfaces.ui.ComponentControl, {
    	
    	execute: function(event, parameters) {
    		var targetList = parameters.target;
    		var selector = parameters.selector;
    		var callback = parameters.callback;
    		
    		if (targetList) {
    			for (var i = 0; i < targetList.length; i++) {
    				var component = document.getElementById(targetList[i]);
    				if (component) {
    	    			richfaces.ui.ComponentControl.invokeOnComponent(event, component, callback);
    				}	
    			}
    		}
    		
    		if(selector) {
    			richfaces.ui.ComponentControl.invokeOnComponent(event, selector, callback);
    		}
    	},
    	
    	invokeOnComponent : function(event, target, callback) {
    		if(callback && typeof callback == 'function') {
	    		$(target).each(function() {
		    		if (this.richfaces && this.richfaces.component) {
		    			callback(event, this.richfaces.component);
		    		}
		    	});
    		}
    	}	
    });

})(jQuery, window.RichFaces);