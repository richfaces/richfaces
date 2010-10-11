// TODO: remove when these functions will be moved to the RichFaces.Event <!--
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
// -->

(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
	rf.ui.InplaceBase =  function(id, options) {
    	$super.constructor.call(this, id);
		this.attachToDom(id);
    	this.namespace = this.getNamespace() || "." + rf.Event.createNamespace(this.getName(), this.id);
		
		this.editEvent = options.editEvent;
        this.noneCss = options.noneCss; 
        this.changedCss = options.changedCss;
        this.defaultLabel = options.defaultLabel;
        this.state = options.state;
                
        this.element = $(document.getElementById(id)); 
        this.editContainer = $(document.getElementById(options.editContainer));
        this.state = options.state;
        
       	this.element.bind(this.editEvent, $.proxy(this.__editHandler, this));

       	this.isSaved = false;
        this.useDefaultLabel = false;

	};
    
	rf.BaseComponent.extend(rf.ui.InplaceBase);
	var $super = rf.ui.InplaceBase.$super;
	
	$.extend(rf.ui.InplaceBase.prototype, ( function () {
		
		var STATE = {
				READY : 'ready', 
	    		CHANGED: 'changed', 
	    		DISABLE: 'disable', 
	    		EDIT: 'edit' 
		};
		
		return {
			getName: function() {
			}, 
			
			getNamespace: function() {
			},
			
       		getValue: function() {
       		},
       		
       		setValue: function(value){
       		}, 
       		
       		getLabel: function() {
       		},
			
       		setLabel: function(value) {
       		}, 
       		
       		onshow: function(){
			}, 
			
			onhide: function() {
			},
			
			getNamespace: function() {
			},
			
			isValueSaved: function() {
				return this.isSaved;
			},
			
			save: function() {
				var value = this.getValue()
       			if(value.length > 0) {
       				this.setLabel(value);
       			} else {
       				this.setLabel(this.defaultLabel);
       				this.useDefaultLabel = true;
       			}
				
				this.isSaved = true;

				this.__applyChangedStyles();
				this.__hide();
			}, 
			
			cancel: function(){
				var text = "";
   				if(!this.useDefaultLabel) {
   					text = this.getLabel()
   				} 
       			this.setValue(text);
       			this.isSaved = true;
           		this.__hide();
			},
			
			__applyChangedStyles: function() {
				if(this.isValueChanged()) {
       				this.element.addClass(this.changedCss);
       			} else {
       				this.element.removeClass(this.changedCss);
       			}
			},
			
			__show: function() {
				this.scrollElements = rf.Event.bindScrollEventHandlers(this.id, this.__scrollHandler, this);
      			this.onshow();
			}, 
			
			__hide: function() {
				if(this.scrollElements) {
					rf.Event.unbindScrollEventHandlers(this.scrollElements, this);
					this.scrollElements = null;
				}
				this.onhide();
      			this.editContainer.addClass(this.noneCss);
			},
			
			__editHandler: function(e) {
   				this.isSaved = false;
      			this.editContainer.removeClass(this.noneCss);
       			this.__show();
       		},       		
       		__scrollHandler: function(e) {
       			this.cancel();
       		},
       		
 			destroy: function () {
       			$super.destroy.call(this);
 			}
		}
	
	})());
	
})(jQuery, window.RichFaces);
