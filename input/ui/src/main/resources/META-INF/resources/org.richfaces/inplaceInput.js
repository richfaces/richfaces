(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
        rf.ui.InplaceInput =  function(id, options) {
        	var mergedOptions = $.extend({}, defaultOptions, options);
        	$super.constructor.call(this, id, mergedOptions);
            this.label = $(document.getElementById(id+"Label"));
            var labelText = this.label.text();
            var inputLabel = this.__getValue();
            this.initialValue = (labelText == inputLabel) ? labelText : "";
            this.saveOnBlur = mergedOptions.saveOnBlur;
            this.showControls = mergedOptions.showControls;
           	this.getInput().bind("focus", $.proxy(this.__editHandler, this));
        	if(this.showControls) {
            	this.okbtn = $(document.getElementById(id+"Okbtn"));
            	this.cancelbtn = $(document.getElementById(id+"Cancelbtn"));
            	this.okbtn.bind("mousedown", $.proxy(this.__saveBtnHandler, this));
            	this.cancelbtn.bind("mousedown", $.proxy(this.__cancelBtnHandler, this));
            }
        	this.focusElement = $(document.getElementById(id+"Focus"));
        };

        rf.ui.InplaceBase.extend(rf.ui.InplaceInput);
    	var $super = rf.ui.InplaceInput.$super;

    	var defaultOptions = {
    	    defaultLabel: "",
    	    saveOnBlur: true,
    	    showControl: true,
    	    noneCss: "rf-ii-none",
    	    readyCss: "rf-ii-d-s",
    	    editCss: "rf-ii-e-s", 
    	    changedCss: "rf-ii-c-s"
    	};

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
           			switch(e.keyCode || e.which) {
           				case rf.KEYS.ESC: 
	       					e.preventDefault();
           					this.cancel(); 
                   			this.onblur(e);
           					break;
           				case rf.KEYS.RETURN:
	       					e.preventDefault();
           					this.save(); 
                   			this.onblur(e);
           					break;
           			}
    			},
    			
           		__blurHandler: function(e) {
    				this.onblur(e);
    			},

    			__isSaveOnBlur: function() {
           			return this.saveOnBlur; 
           		},
           		
           		__setInputFocus: function() {
           			this.getInput().unbind("focus", this.__editHandler);
           			this.getInput().focus();
           		},
           		
           		__saveBtnHandler: function(e) {
           			this.cancelButton = false; 
           			this.save();
           			this.onblur(e);
           		}, 
           		
           		__cancelBtnHandler: function(e) {
           			this.cancelButton = true; 
           			this.cancel();
           			this.onblur(e);
           		},
           		
           		__editHandler: function(e){
           			$super.__editHandler.call(this,e);
           			this.onfocus(e);
           		},
          		
           		getLabel: function() {
           			return this.label.text();
           		},

           		setLabel: function(value) {
           			this.label.text(value);
           		}, 
           		
           		isValueChanged: function () {
           			return (this.__getValue() != this.initialValue);
           		},
    			
           		onshow: function(){
           			this.__setInputFocus();
    			}, 
    			
    			onhide: function() {
    				this.focusElement.focus();
    			},
    			
    			onfocus: function(e) {
    				if(!this.__isFocused()) {
    					this.__setFocused(true);
	    				this.focusValue = this.__getValue();
						this.invokeEvent.call(this, "focus", document.getElementById(this.id + 'Input'), e);
    				}
    			},
    			
    			onblur: function(e) { 
    				if(this.__isFocused()) {
    					this.__setFocused(false);
	    				this.invokeEvent.call(this, "blur", document.getElementById(this.id + 'Input'), e);
	    				
	    				if(this.isValueSaved() || this.__isSaveOnBlur()) {
	           				this.save();
	           			} else {
	               			this.cancel();
	           			}
	    				
           				this.__hide();
	    				
	    				if(!this.cancelButton) {
	       					if(this.__isValueChanged()) {
	    						this.invokeEvent.call(this, "change", document.getElementById(this.id + 'Input'), e);
	       					}
	       				}
	           			this.getInput().bind("focus", $.proxy(this.__editHandler, this));
    				}
    			}, 
    			
    			__isValueChanged: function() {
    				return (this.focusValue != this.__getValue());
    			},
    			
    			__setFocused: function(focused) {
    				this.focused = focused;
    			}, 
    			
    			__isFocused: function() {
    				return this.focused;
    			}
    		}
    	})());

})(jQuery, window.RichFaces);