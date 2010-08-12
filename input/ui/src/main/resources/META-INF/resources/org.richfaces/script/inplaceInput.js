(function ($, richfaces) {
	
	richfaces.ui = richfaces.ui || {};
      
        richfaces.ui.InplaceInput =  function(id, options) {
            this.options = options;
            this.currentState = options.state;
            this.editEvent = options.editEvent;
            this.noneCss = options.noneCss; 
            this.changedCss = options.changedCss;
                        
            this.element = $(document.getElementById(id)); 
            this.editContainer = $(document.getElementById(options.editContainer));
            this.input = $(document.getElementById(options.input));
            this.label = $(document.getElementById(options.label));
            this.initialValue = this.label.text();
            
            richfaces.Event.bind(this.element, this.editEvent, this.editState, this);
            richfaces.Event.bind(this.input, "change", this.changedState, this);
            richfaces.Event.bind(this.input, "blur", this.readyState, this);
            
            if(options.showControls) {
            	this.okbtn = $(document.getElementById(options.okbtn));
            	this.cancelbtn = $(document.getElementById(options.cancelbtn));
            	richfaces.Event.bind(this.okbtn, "click", this.clickOk, this);
            	richfaces.Event.bind(this.cancelbtn, "click", this.clickCancel, this);
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
           		
           		switchToState: function(state) {
           			if(this.currentState != state) {
           				var states = RichFaces.ui.InplaceInput;	
						switch(state) {
							case states.READY: this.doReady(); break;
				 			case states.EDIT: this.doEdit(); break;
							case states.CHANGED: this.doChanged(); break;
							default:	
						}
      	   				this.currentState = state;
           			}
           		},
           		
           		editState: function(){
           			this.switchToState(RichFaces.ui.InplaceInput.EDIT);
           		},
			
           		changedState: function(){
           			this.switchToState(RichFaces.ui.InplaceInput.CHANGED);
           		},

           		readyState: function() {
          			this.switchToState(RichFaces.ui.InplaceInput.READY);
           		},

           		doEdit: function() {
           			this.editContainer.removeClass(this.noneCss);
           			this.input.focus();	
           		}, 
			
           		doChanged: function() {
           			var inputValue = this.input.val();
           			if(inputValue.length > 0) {
           				this.label.text(inputValue);
           			}
           			
           			if(inputValue != this.initialValue) {
           				this.element.addClass(this.changedCss);
               			this.editContainer.addClass(this.noneCss);
           			} 
           		}, 
				
           		doReady: function() {
           			if(this.initialValue == this.input.val()) {
           				this.element.removeClass(this.changedCss);
           			}
           			this.editContainer.addClass(this.noneCss);
           		},
           		
           		clickOk: function() {
           			alert('click');
           		}, 
           		
           		clickCancel: function() {
           			alert('cancel');
           		}
           	}
        })());

})(jQuery, window.RichFaces);

