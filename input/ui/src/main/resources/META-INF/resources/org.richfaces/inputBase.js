(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
        rf.ui.InputBase =  function(id, options) {
        	$super.constructor.call(this, id);
        	this.namespace = this.getNamespace() || "." + rf.Event.createNamespace(this.getName(), this.getId());

        	this.id = id;
        	this.input = $(document.getElementById(id + "Input"));
    		this.attachToDom(id);
    		
    		this.input.bind("keydown", $.proxy(this.__keydownHandler, this));
           	this.input.bind("blur", $.proxy(this.__blurHandler, this));
           	this.input.bind("change", $.proxy(this.__changeHandler, this));
           	this.input.bind("focus", $.proxy(this.__focusHandler, this));
        };

        rf.BaseComponent.extend(rf.ui.InputBase);
    	var $super = rf.ui.InputBase.$super;
    	
    	$.extend(rf.ui.InputBase.prototype, ( function () {

    		return {

    			name : "inputBase",
    			
    			
    			getName: function() {
    				return this.name;
    			},
    			
    			getNamespace: function() {
    				return this.namespace;
    			},
    			
    			__focusHandler: function(e) {
    			},
    			
           		__keydownHandler: function(e) {
    			},
    			
           		__blurHandler: function(e) {
           		},
    			
           		__changeHandler: function(e) {
           		},
           		
           		__setInputFocus: function() {
           			this.input.focus();
           		},
           		
           		getValue: function() {
    				return this.input.val();
           		},
           		
           		setValue: function(value){
           			this.input.val(value);
           		},
           		
           		getInput: function() {
           			return this.input;
           		},
           	
           		getId: function() {
           			return	this.id;
           		} 
    		}
    	})());

})(jQuery, window.RichFaces);