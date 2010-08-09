(function ($, richfaces) {

	richfaces.ui = richfaces.ui || {};
  
	richfaces.ui.SubTable =  function(id, f, options) {
		this.id = id;
		this.stateInput = options.stateInput;
		this.optionsInput = options.optionsInput;
		this.expandMode = options.expandMode;
		this.eventOptions = options.eventOptions;
		this.formId = f;
		
    	$super.constructor.call(this, id);
    	this.attachToDom(id);
    };
	
	$.extend(richfaces.ui.SubTable, {
		MODE_AJAX: "ajax",
		MODE_SRV: "server", 
		MODE_CLNT: "client",
		collapse: 0,
		expand: 1
	})
	
	var $super = richfaces.BaseComponent.extend(richfaces.ui.SubTable);
    var $p = richfaces.BaseComponent.extend(richfaces.ui.SubTable, {});
    var $super = richfaces.ui.SubTable.$super;

    $.extend(richfaces.ui.SubTable.prototype, (function () {
    	
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
    		
    		name: "RichFaces.ui.SubTable",

    		toggle: function(e, options) {
    			if(this.expandMode == richfaces.ui.SubTable.MODE_AJAX) {
           			ajax.call(this, e, this.eventOptions, options);
           		}else if(this.expandMode == richfaces.ui.SubTable.MODE_SRV) {
           			server.call(this, options);
           		}else if(this.expandMode == richfaces.ui.SubTable.MODE_CLNT) {
           			client.call(this, options);
           		}
    		}, 
    		
    		collapse: function(options) {
    			this.setState(richfaces.ui.SubTable.collapse);
    			element.call(this).hide();
    		}, 
    		
    		expand: function(options) {
    			this.setState(richfaces.ui.SubTable.expand);
    			element.call(this).show();
    		},
    		    		    		
    		isExpand: function() {
    			return (this.getState() > richfaces.ui.SubTable.collapse);
           	},
           
    		switchState: function(options) {
    			var state = this.isExpand() ? richfaces.ui.SubTable.collapse : richfaces.ui.SubTable.expand;
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
    		}	
    	};

    })());

})(jQuery, window.RichFaces);