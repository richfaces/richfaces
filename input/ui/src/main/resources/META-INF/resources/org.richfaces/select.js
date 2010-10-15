(function ($, rf) {
	
	rf.ui = rf.ui || {};
		
        rf.ui.Select =  function(id, options) {
        	this.id = id;

        	// TODO: move to defaultOptions ??
        	options['attachTo'] = id;
        	options['attachToBody'] = true;
        	
        	this.selValueInput = $(document.getElementById(options.selValueInput));
        	
        	this.input = $(document.getElementById(id+"Input"));
        	this.input.bind("click", $.proxy(this.__clickHandler, this));
        	this.input.bind("keydown", $.proxy(this.__keydownHandler, this));
        	this.input.bind("blur", $.proxy(this.__blurHandler, this));
        	
        	this.items = options.items;
        	
        	if(options.showControl) {
        		this.btn = $(document.getElementById(id+"Button"));
            	this.btn.bind("click", $.proxy(this.__clickHandler, this));
        	}
        	this.popupList = new rf.ui.SelectList(options.list, this, options);
        };
        
    	rf.BaseComponent.extend(rf.ui.Select);
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
    			
           		__setInputFocus: function() {
           			this.input.focus();
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
    			}, 
    			
    	 		setValue: function(value){
           			this.input.val(value);
           		}
    		}
    		
    	})());

})(jQuery, window.RichFaces);
