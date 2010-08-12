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
            
            richfaces.Event.bind(this.element, this.editEvent, this.editState, this);
            richfaces.Event.bind(this.input, "change", this.changedState, this);
            richfaces.Event.bind(this.input, "blur", this.readyState, this);
            
            if(this.showControls) {
            	this.okbtn = $(document.getElementById(options.okbtn));
            	this.cancelbtn = $(document.getElementById(options.cancelbtn));
            	richfaces.Event.bind(this.okbtn, "mousedown", this.clickOk, this);
            	richfaces.Event.bind(this.cancelbtn, "mousedown", this.clickCancel, this);
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
           			if(!this.showControls) {
           				this.switchToState(RichFaces.ui.InplaceInput.READY);
           			}	
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
           			} else {
           				this.element.removeClass(this.changedCss);
           			}
           		}, 
				
           		doReady: function() {
           			this.editContainer.addClass(this.noneCss);
           		},
           		
           		clickOk: function() {
           			this.changedState();
       				this.switchToState(RichFaces.ui.InplaceInput.READY);
           			return false;
           		}, 
           		
           		clickCancel: function() {
           			var text = this.label.text();
           			this.input.val(text);
       				this.switchToState(RichFaces.ui.InplaceInput.READY);
           			return false;
           		}
           	}
        })());

})(jQuery, window.RichFaces);

