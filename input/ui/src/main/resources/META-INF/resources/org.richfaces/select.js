//TODO:  to the utils? 
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
		
        rf.ui.Select =  function(id, options) {
        	$super.constructor.call(this, id);
        	this.id = id;

        	// TODO: move to defaultOptions ??
        	options['attachTo'] = id;
        	options['attachToBody'] = true;
        	
        	this.selValueInput = $(document.getElementById(options.selValueInput));
        	this.items = options.items;
        	
        	if(options.showControl) {
        		this.btn = $(document.getElementById(id+"Button"));
            	this.btn.bind("click", $.proxy(this.__clickHandler, this));
        	}
        	this.popupList = new rf.ui.PopupList(options.list, this, options);
        };
        
    	rf.ui.InputBase.extend(rf.ui.Select);
    	var $super = rf.ui.Select.$super;
        
    	$.extend(rf.ui.Select.prototype, ( function () {

    		return{
    			name : "select", 
    			
    			__clickHandler: function(e) {
    				e.preventDefault();
    				if(!this.popupList.isVisible()) {
    					this.popupList.show();
    				} else {
    					this.popupList.hide();
    				}
    				this.__setInputFocus();
    			}, 
		
    			__keydownHandler: function(e) {
    				var code; 
    				
    				if(e.keyCode) {
    					code = e.keyCode;
    				} else if(e.which) {
    					code = e.which;
    				}
           			
    				if(this.popupList.isVisible()) {
    	       			switch(code) {
    	       				case rf.KEYS.DOWN: 
    	       					e.preventDefault();
    	       					this.popupList.__selectNext(); 
    	       	           		this.__setInputFocus();
    	       					break;
    	       				
    	       				case rf.KEYS.UP:
    	       					e.preventDefault();
    	       					this.popupList.__selectPrev();
    	       	           		this.__setInputFocus();
    	       					break;
    	       				
    	       				case rf.KEYS.RETURN:
    	       					e.preventDefault();
    	       					this.popupList.__selectCurrent();
    	       	           		this.__setInputFocus();
    	       					return false;
    	       					break;
    	   				}
    				}
    			}, 
           		
    			__blurHandler: function(e) {
    				var target = $(e.originalEvent.explicitOriginalTarget);
    				if(!this.popupList.isPopupList(target)) {
    					this.popupList.hide();
    					return true;
    				} 				
           			return false;
           		},
    			
    			processItem: function(item) {
    				var key = $(item).attr("id");
    				var value = this.getItemValue(key);
    				this.saveItemValue(value);
    				var label = this.getItemLabel(key);
    				this.setValue(label);
               		this.popupList.hide();
               		this.__setInputFocus();
    			}, 
    			
    			getItemValue: function(key) {
    				for(var i in this.items) {
    					var item = this.items[i];
    					if(item && item.id == key) {
    						return item.value;
    					}
    				}
    			}, 
    			
    			getItemLabel: function(key) {
    				for(var i in this.items) {
    					var item = this.items[i];
    					if(item && item.id == key) {
    						return item.label;
    					}
    				}
    			}, 
    			
    			saveItemValue: function(value) {
    				this.selValueInput.val(value);
    			}
    		}
    		
    	})());

})(jQuery, window.RichFaces);
