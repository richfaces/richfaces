(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
        rf.ui.InplaceInput =  function(id, options) {
        	$super.constructor.call(this, id, options);

        	this.input = $(document.getElementById(options.input));
            this.label = $(document.getElementById(options.label));

            var label = this.label.text();
            var inputLabel = this.input.val();
            this.initialValue = (label == inputLabel) ? label : "";
            this.saveOnBlur = options.saveOnBlur;
            this.showControls = options.showControls;

        	this.input.bind("focus", $.proxy(this.__editHandler, this));
           	this.input.bind("change", $.proxy(this.__changeHandler, this));
           	this.input.bind("blur", $.proxy(this.__blurHandler, this));
           	this.input.bind("keydown", $.proxy(this.__keydownHandler, this));
           	
            if(this.showControls) {
            	this.okbtn = $(document.getElementById(options.okbtn));
            	this.cancelbtn = $(document.getElementById(options.cancelbtn));
            	this.okbtn.bind("mousedown", $.proxy(this.__saveBtnHandler, this));
            	this.cancelbtn.bind("mousedown", $.proxy(this.__cancelBtnHandler, this));
            }
           	
            this.focusElement = $(document.getElementById(options.focusElement));
        };

        rf.ui.InplaceBase.extend(rf.ui.InplaceInput);
    	var $super = rf.ui.InplaceInput.$super;
    	
    	$.extend(rf.ui.InplaceInput.prototype, ( function () {

    		return {

    			name : "inplaceInput",
    			
    			geName: function() {
    				return this.name;
    			},
    			
    			geNamespace: function() {
    				return this.namespace;
    			},
           		
           		__keydownHandler: function(e) {
    				var code; 
    				
    				if(e.keyCode) {
    					code = e.keyCode;
    				} else if(e.which) {
    					code = e.which;
    				}
    				
           			switch(code) {
           				case 27: 
	       					e.preventDefault();
           					this.cancel(); 
           					break;
           				case 13:
	       					e.preventDefault();
           					this.save(); 
           					return false;
           					break;
           			}
    			},
    			
           		__blurHandler: function(e) {
           			if(!this.isValueSaved() && this.__isSaveOnBlur()) {
           				this.save();
           			} else {
           				this.__hide();
           			}
           			return false;
           		},
    			
           		__changeHandler: function(e) {
           			if(!this.isValueSaved()) {
           				this.save();
           			}
           		}, 
           		
           		__isSaveOnBlur: function() {
           			return this.saveOnBlur; 
           		},
           		
           		__setInputFocus: function() {
           			this.input.unbind("focus", this.__editHandler);
           			this.input.focus();
           			this.input.bind("focus", $.proxy(this.__editHandler, this));
           		},
           		
           		__saveBtnHandler: function(e) {
           			this.save();
           			return false;
           		}, 
           		
           		__cancelBtnHandler: function(e) {
           			this.cancel();
           			return false;
           		},

           		getValue: function() {
    				return this.input.val();
           		},
           		
           		setValue: function(value){
           			this.input.val(value);
           		}, 
           		
           		getLabel: function() {
           			return this.label.text();
           		}, 

           		setLabel: function(value) {
           			this.label.text(value);
           		}, 
           		
           		isValueChanged: function () {
           			return (this.getValue() != this.initialValue);
           		},
    			
           		onshow: function(){
           			this.__setInputFocus();
    			}, 
    			
    			onhide: function() {
    				this.focusElement.focus();
    			}   			

    		}
    	})());

})(jQuery, window.RichFaces);