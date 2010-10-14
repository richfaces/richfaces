(function ($, rf) {
	
	rf.ui = rf.ui || {};
		
	rf.ui.Popup =  function(id, options) {
       	this.id = id;
		this.attachToDom(id);
       	this.popup = $(document.getElementById(id));

       	this.visible = options.visible;
       	this.attachTo = options.attachTo;
       	this.attachToBody = options.attachToBody;

       	this.popup.bind("mouseover", $.proxy(this.__onMouseOver, this));
       	this.popup.bind("click", $.proxy(this.__onClick, this));
	};
        
	rf.BaseComponent.extend(rf.ui.Popup);
	var $super = rf.ui.Popup.$super;
    
	$.extend(rf.ui.Popup.prototype, (function () {

		return{

			name : "popup", 
           			
			show: function() {
				if(!this.visible) {
					if(this.attachToBody) {
						this.parentElement = this.popup.parent();
						this.popup.detach().appendTo("body");	
					}
					this.popup.setPosition({id: this.attachTo}, {type:"DROPDOWN", offset:[0,20]}).show();
					this.visible = true;
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
			}
		}	
		
    })());

})(jQuery, window.RichFaces);