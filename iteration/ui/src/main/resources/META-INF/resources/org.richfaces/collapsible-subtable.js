(function ($, richfaces) {

	richfaces.ui = richfaces.ui || {};
  
	richfaces.ui.CollapsibleSubTable =  function(id, f, options) {
		this.id = id;
		this.stateInput = options.stateInput;
		this.optionsInput = options.optionsInput;
		this.expandMode = options.expandMode;
		this.eventOptions = options.eventOptions;
		this.formId = f;
		
		this.attachToDom();
    };
	
	$.extend(richfaces.ui.CollapsibleSubTable, {
		MODE_AJAX: "ajax",
		MODE_SRV: "server", 
		MODE_CLNT: "client",
		collapse: 0,
		expand: 1
	});

    	richfaces.BaseComponent.extend(richfaces.ui.CollapsibleSubTable);
    	var $super = richfaces.ui.CollapsibleSubTable.$super;

    $.extend(richfaces.ui.CollapsibleSubTable.prototype, (function () {
    	
    	var element = function() { 
    		//use parent tbody as parent dom elem
    		return $(document.getElementById(this.id)).parent();
    	};
    	
    	var stateInputElem = function() {
    		return $(document.getElementById(this.stateInput));
    	};
    	
    	var optionsInputElem = function() {
    		return $(document.getElementById(this.optionsInput));
    	};
    	
    	var ajax = function(e, options) {
			this.switchState();
			richfaces.ajax(this.id, e, options);
		};
		
		var server = function(options) {
			this.switchState();
			$(document.getElementById(this.formId)).submit();
		};
		
		var client = function(options) {
			if(this.isExpand()) {
				this.collapse(options);
			} else {
				this.expand(options);
			}
		};		
				
     	  
    	return {
    		
    		name: "CollapsibleSubTable",

    		toggle: function(e, options) {
    			if(this.expandMode == richfaces.ui.CollapsibleSubTable.MODE_AJAX) {
           			ajax.call(this, e, this.eventOptions, options);
           		}else if(this.expandMode == richfaces.ui.CollapsibleSubTable.MODE_SRV) {
           			server.call(this, options);
           		}else if(this.expandMode == richfaces.ui.CollapsibleSubTable.MODE_CLNT) {
           			client.call(this, options);
           		}
    		}, 
    		
    		collapse: function(options) {
    			this.setState(richfaces.ui.CollapsibleSubTable.collapse);
    			element.call(this).hide();
    		}, 
    		
    		expand: function(options) {
    			this.setState(richfaces.ui.CollapsibleSubTable.expand);
    			element.call(this).show();
    		},
    		    		    		
    		isExpand: function() {
    			return (parseInt(this.getState()) == richfaces.ui.CollapsibleSubTable.expand);
           	},
           
    		switchState: function(options) {
    			var state = this.isExpand() ? richfaces.ui.CollapsibleSubTable.collapse : richfaces.ui.CollapsibleSubTable.expand;
    			this.setState(state);
    		}, 
    		
    		getState: function() {
    			return stateInputElem.call(this).val();
    		},
    		
    		setState: function(state) {
    			stateInputElem.call(this).val(state)
    		},
    		
    		setOption: function(option) {
    			optionsInputElem.call(this).val(option);
    		}, 
    		
    		getMode: function() {
    			return this.expandMode;
    		},
		destroy: function() {
			this.detach();
			$super.destroy.call(this);
		}
    	};

    })());

})(jQuery, window.RichFaces);