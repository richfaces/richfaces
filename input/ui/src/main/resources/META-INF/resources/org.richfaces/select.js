(function ($, rf) {
	
	rf.ui = rf.ui || {};
		
		var INTERFACE = {
			SelectListener : { 
				processItem: function(event, element){}
			}	
		};
		
        rf.ui.Select =  function(id, listener, options) {
        	this.id = id;
        	this.select = $(document.getElementById(id));
        	this.itemsCord = $(document.getElementById(options.itemsCord));
        	this.selectItemCss = options.selectItemCss;
        	this.itemCss = options.itemCss;
            this.selectListener =  listener;

            this.select.bind("blur", $.proxy(this.__blurHandler, this));
        	this.itemsCord.bind("mouseover", $.proxy(this.__mouseHandler, this));
           	this.itemsCord.bind("click", $.proxy(this.__mouseHandler, this));
        };
        
    	rf.BaseComponent.extend(rf.ui.Select);
    	var $super = rf.ui.Select.$super;
        
    	$.extend(rf.ui.Select.prototype, ( function () {
    		
    		var processed = null;
    		
    		var isSelectListener = function(obj) {
    			for (var method in INTERFACE.SelectListener) {
    		        if ( (typeof obj[method] != typeof INTERFACE.SelectListener[method]) ) {
    		        	return false;
    		        }
    		    }
    		    return true;
    		};
    		
    		return{
    			name : "select", 
           			
           		show: function() {
    				this.select.css("display", "");
           		}, 
           		
           		hide: function() {
    				this.select.css("display", "none");
           		},
           		
           		processItem: function(event, element) {
           			if(isSelectListener(this.selectListener)) {
           				this.selectListener.processItem(event, element);
           			}
           		},
           		
           		__getCurrentElement: function() {
           			return processed;
           		},
           		
           		__blurHandler: function(e) {
    				processItem(e, processed);
    				return false;
           		},

				__mouseHandler: function(event) {
           			var element = $(event.target).closest("."+this.itemCss, event.currentTarget);
           			if (event&& element) {
           				if(event.type == 'mouseover') {
	           				if(processed) {
	           					processed.removeClass(this.selectItemCss);
	           				}
	           				element.addClass(this.selectItemCss);
	           				processed = element;
           				}
           				
           				if(event.type == 'click') {
           					this.processItem(event, element);
           				}
           			}
   					return false;
           		}, 
           		
           		__getId: function() {
           			return this.id;
           		}
    		}
    	})());

})(jQuery, window.RichFaces);
