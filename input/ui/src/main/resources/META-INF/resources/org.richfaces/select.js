//TODO: utils?
(function (rf) {
	rf.KEYS = {
		BACKSPACE: 8,	
		TAB: 9,
		RETURN: 13,
		ESC: 27,
		PAGEUP: 33,
		PAGEDOWN: 34,
		LEFT: 37,
		UP: 38,
		RIGHT: 39,
		DOWN: 40,
		DEL: 46
	};
})(RichFaces);

(function ($, rf) {
	
	rf.ui = rf.ui || {};
		
		var INTERFACE = {
			SelectListener : { 
				processItem: function(e, element){}
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
           	
           	this.index = -1;
           	//TODO: from option map?
           	this.visible = false;
           	this.updateItemsList();
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
    				this.visible = true;
           		}, 
           		
           		hide: function() {
    				this.select.css("display", "none");
    				this.visible = false;
           		},
           		
           		processItem: function(e, element) {
           			if(isSelectListener(this.selectListener)) {
           				this.selectListener.processItem(e, element);
           			}
           		},
           		
           		isVisible: function() {
           			return this.visible;
           		},
           		
           		__selectItem: function(e, index) {
           			var item;
         			
         			if (this.index != -1) {
						item = this.items.eq(this.index);
						item.removeClass(this.selectItemCss);
					}
					
					this.index += index;
					if ( this.index < 0 ) {
						this.index = this.items.length - 1;
					} else if (this.index >= this.items.length) {
						this.index = 0;
					}
					
           			item = this.items.eq(this.index);
           			item.addClass(this.selectItemCss);
           		},
           		
           		__onEnter: function(e) {
           		
           		},
           		
           		__onKeyUp: function(e) {
           			this.__selectItem(e, -1);
           		},
           		
           		__onKeyDown: function(e) {
           			this.__selectItem(e, 1);
           		},
           		
           		__getCurrentElement: function() {
           			return processed;
           		},
           		
           		__blurHandler: function(e) {
    				processItem(e, processed);
    				return false;
           		},

				__mouseHandler: function(e) {
           			var element = $(e.target).closest("."+this.itemCss, e.currentTarget);
           			if (e && element) {
           				if(e.type == 'mouseover') {
	           				if(processed) {
	           					processed.removeClass(this.selectItemCss);
	           				}
	           				element.addClass(this.selectItemCss);
	           				processed = element;
           				}
           				
           				if(e.type == 'click') {
           					this.processItem(e, element);
           				}
           			}
   					return false;
           		},
           		
       			updateItemsList: function () {
					this.items = this.itemsCord.find("."+this.itemCss);
				},
				
				__getItems: function () {
					return this.items;
				},
           		
           		__getId: function() {
           			return this.id;
           		}
    		}
    	})());

})(jQuery, window.RichFaces);
