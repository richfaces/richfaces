(function ($, richfaces) {
	
	richfaces.ui = richfaces.ui || {};
      
        richfaces.ui.InplaceInput =  function(id, options) {
            this.currentState = options.state;
            this.editEvent = options.editEvent;
            this.noneCss = options.noneCss; 
            this.changedCss = options.changedCss;
            this.showControls = options.showControls;
            
            this.element = $(document.getElementById(id)); 
            this.editContainer = $(document.getElementById(options.editContainer));
            this.input = $(document.getElementById(options.input));
            this.label = $(document.getElementById(options.label));
            this.initialValue = this.label.text();
            
            richfaces.Event.bind(this.element, this.editEvent, this.edit, this);
            richfaces.Event.bind(this.input, "change", this.save, this);
            richfaces.Event.bind(this.input, "blur", this.save, this);
            richfaces.Event.bind(this.input, "focus", this.edit, this);

            if(this.showControls) {
            	this.okbtn = $(document.getElementById(options.okbtn));
            	this.cancelbtn = $(document.getElementById(options.cancelbtn));
            	richfaces.Event.bind(this.okbtn, "mousedown", this.__saveBtnHandler, this);
            	richfaces.Event.bind(this.cancelbtn, "mousedown", this.__cancelBtnHandler, this);
            }
        };
        
    	$.extend(richfaces.ui.InplaceInput, {
    		READY : "ready",
    		EDIT : "edit",
    		CHANGED : "changed"
    	});
    	
    	$.extend(richfaces.ui.InplaceInput.prototype, ( function () {
           	return {
           		name : "RichFaces.ui.InplaceInput",

/******************  public methods  *****************************************/
           		
           		edit: function() {
           			this.editContainer.removeClass(this.noneCss);
           			this.input.focus();	
           		}, 
           		
           		save: function() {
           			var inputValue = this.input.val();
           			if(inputValue.length > 0) {
           				this.label.text(inputValue);
           			}
           			
           			if(inputValue != this.initialValue) {
           				this.element.addClass(this.changedCss);
           			} else {
           				this.element.removeClass(this.changedCss);
           			}
           			
           			if(!this.showControls) {
           				this.editContainer.addClass(this.noneCss);
           			}
           		}, 
           		
           		cancel: function() {
           			var text = this.label.text();
           			this.input.val(text);
           			this.editContainer.addClass(this.noneCss);
           		},
           		
           		setValue: function (value) {
           			this.input.val(value);
           			this.save();
           		},
           		
           		getValue: function() {
           			return this.input.val();
           		}, 

/******************  private methods  *****************************************/

           		__saveBtnHandler: function() {
           			this.save();
           			this.editContainer.addClass(this.noneCss);
           			return false;
           		}, 
           		
           		__cancelBtnHandler: function() {
           			this.cancel(); return false;
           		}
           	}
           	})());

})(jQuery, window.RichFaces);

