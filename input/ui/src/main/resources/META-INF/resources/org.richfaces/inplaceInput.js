// TODO: remove when these functions will be moved to the RichFaces.Event
$.extend(RichFaces.Event, {
	bindScrollEventHandlers: function(element, handler, component) {
		var elements = [];
		element = RichFaces.getDomElement(element).parentNode;
		while (element && element!=window.document.body)
		{
			if (element.offsetWidth!=element.scrollWidth || element.offsetHeight!=element.scrollHeight)
			{
				elements.push(element);
				RichFaces.Event.bind(element, "scroll"+component.getNamespace(), handler, component);
			}
			element = element.parentNode;
		}
		return elements;
	},
	unbindScrollEventHandlers: function(elements, component) {
		RichFaces.Event.unbind(elements, "scroll"+component.getNamespace());
	}
});

(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
        rf.ui.InplaceInput =  function(id, options) {
        	/*TODO: use defaultOptions*/ 
        	$super.constructor.call(this, id);
    		this.attachToDom(id);

    		this.namespace = this.namespace || "."+rf.Event.createNamespace(this.name, this.id);

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
            
            this.element.bind(this.editEvent, $.proxy(this.__editHandler, this));
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
        };
    	
    	// Extend component class and add protected methods from parent class to our container
    	rf.BaseComponent.extend( rf.ui.InplaceInput);
    	
    	// define super class link
    	var $super = rf.ui.InplaceInput.$super;

    	$.extend(rf.ui.InplaceInput.prototype, ( function () {
    		
    		var isSaved = false;
           
    		return {
           		name : "inplaceInput",

/******************  public methods  *****************************************/
           		
           		getNamespace: function () {
     				return this.namespace;
     			},
           		
           		edit: function() {
       				isSaved = false;
       				this.__show();
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
           			isSaved = true;
       				this.__hide();
           		}, 
           		
           		cancel: function() {
           			var text = this.label.text();
           			this.input.val(text);
           			isSaved = true;
               		this.__hide();
           		},
           		
           		setValue: function (value) {
           			this.input.val(value);
           			this.save();
           		},
           		
           		getValue: function() {
           			return this.input.val();
           		}, 

/******************  private methods  *****************************************/

           		__saveBtnHandler: function(e) {
           			this.save();
           			return false;
           		}, 
           		
           		__cancelBtnHandler: function(e) {
           			this.cancel();
           			return false;
           		}, 
           		
           		__editHandler: function(e) {
           			this.input.unbind("focus", this.__editHandler);
           			this.edit();
           			this.input.bind("focus", $.proxy(this.__editHandler, this));
           		}, 
           		
           		__changeHandler: function(e) {
           			if(!isSaved) {
           				this.save();
           			}
           		}, 
           		
           		__blurHandler: function(e) {
           			if(!isSaved) {
           				this.save();
           			}
           		},
           		
           		__scrollHandler: function(e) {
           			this.cancel();
           		},
           		
           		__keydownHandler: function(e) {
           			switch(e.keyCode) {
           				/*Esc*/
           				case 27: 
           					this.cancel(); 
           					break;
           				/*Enter*/	
           				case 13:
           					this.save(); 
           					return false;
           			}
           			
           		},
           		           		
           		__show: function() {
    				this.scrollElements = rf.Event.bindScrollEventHandlers(this.id, this.__scrollHandler, this);
          			this.editContainer.removeClass(this.noneCss);
           		},
           		
           		__hide: function() {
        			rf.Event.unbindScrollEventHandlers(this.scrollElements, this);
        			this.scrollElements = null;
           			this.editContainer.addClass(this.noneCss);
   					this.input.blur();
           		},

     			destroy: function () {
           			//TODO: unbind handlers
           			$super.destroy.call(this);
     			}
           	}
           	})());

})(jQuery, window.RichFaces);

