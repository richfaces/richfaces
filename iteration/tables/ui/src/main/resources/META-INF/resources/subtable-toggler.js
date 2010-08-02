(function ($, richfaces) {

	richfaces.ui = richfaces.ui || {};
  
	richfaces.ui.SubTableToggler =  function(id, options) {
		this.id = id;
		this.eventName = options.eventName;
        this.expandControl = options.expandControl;
        this.collapseControl = options.collapseControl;
        this.forId = options.forId;
        
        richfaces.Event.bindById(this.id, this.eventName, this.toggle, this);
     };
     
     $.extend(richfaces.ui.SubTableToggler.prototype, (function () {
        
    	 var getElementById= function(id) {
    		 return $(document.getElementById(id))
    	 }
    	            	 
	 	 return {
	 			 		 
	 	 	toggle: function(e) {
	 	 		var subtable = richfaces.$(this.forId);
		 		if(subtable) {
		 			var mode = subtable.getMode();
		 			
		 			if(richfaces.ui.SubTable.MODE_CLNT == mode) {
		 				this.toggleControl(subtable.isExpand());
		 			}
		 			
		 			subtable.setOption(this.id);
		 			subtable.toggle(e);
		 		}
	 	 	}, 
    	 	
    	 	toggleControl: function(expanded) {
    	 		var expandControl = getElementById(this.expandControl);
    	 		var collapseControl = getElementById(this.collapseControl);
             
    	 		if(expanded) {
    	 			collapseControl.hide();    
    	 			expandControl.show();
    	 		} else {
    	 			expandControl.hide();
    	 			collapseControl.show();
    	 		}
    	 	}
	 	 
         };

     })());

})(jQuery, window.RichFaces);