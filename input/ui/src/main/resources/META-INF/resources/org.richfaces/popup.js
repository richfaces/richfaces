(function ($, rf) {
	
	rf.ui = rf.ui || {};
		
	rf.ui.Popup =  function(id, options) {
       	this.id = id;
		this.attachToDom(id);
       	this.popup = $(document.getElementById(id));

       	var mergedOptions = $.extend({}, defaultOptions, options);
       	this.visible = mergedOptions.visible;
       	this.attachTo = mergedOptions.attachTo;
       	this.attachToBody = mergedOptions.attachToBody;
       	this.positionType = mergedOptions.positionType;
       	this.positionOffset = mergedOptions.positionOffset;

       	this.popup.bind("mouseover", $.proxy(this.__onMouseOver, this));
       	this.popup.bind("click", $.proxy(this.__onClick, this));
	};
        
	rf.BaseComponent.extend(rf.ui.Popup);
	var $super = rf.ui.Popup.$super;
	

	var defaultOptions = {
		visible: false	
	};
    
	$.extend(rf.ui.Popup.prototype, (function () {

		return{

			name : "popup", 
           			
			show: function() {
				if(!this.visible) {
					if(this.attachToBody) {
						this.parentElement = this.popup.parent();
						this.popup.detach().appendTo("body");	
					}
					this.popup.setPosition({id: this.attachTo}, {type: this.positionType , offset: this.positionOffset}).show();
					this.visible = true;
					this.__onShow();
				}
			}, 
           		
			hide: function() {
				if(this.visible) {
					this.popup.hide();
					this.visible = false;
					if (this.attachToBody && this.parentElement) {
						this.popup.detach().appendTo(this.parentElement);
						this.parentElement = null;
					}
					this.__onHide();
				}
			},
           		
			isVisible: function() {
				return this.visible;
			}, 
			
			getId: function() {
				return this.id;
			},
						
			__onMouseOver: function(e) {
			},            		
			
			__onClick: function(e) {
			}, 
			
			__onHide: function() {
			}, 
			
			__onShow: function() {
				
			}
		}	
		
    })());

})(jQuery, window.RichFaces);