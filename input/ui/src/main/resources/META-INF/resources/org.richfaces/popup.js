(function ($, rf) {
	
	rf.ui = rf.ui || {};
		
	rf.ui.Popup =  function(id, options) {
       	this.id = id;
		this.attachToDom(id);
       	this.popup = $(document.getElementById(id));
       	this.visible = options.visible;
		
       	this.popup.bind("mouseover", $.proxy(this.__onMouseOver, this));
       	this.popup.bind("click", $.proxy(this.__onClick, this));
	};
        
	rf.BaseComponent.extend(rf.ui.Popup);
	var $super = rf.ui.Popup.$super;
    
	$.extend(rf.ui.Popup.prototype, (function () {

		return{

			name : "popup", 
           			
			show: function() {
				this.popup.css('display', '');
				this.visible = true;
			}, 
           		
			hide: function() {
				this.popup.css('display', 'none');
				this.visible = false;
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