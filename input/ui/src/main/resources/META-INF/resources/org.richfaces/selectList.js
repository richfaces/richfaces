(function ($, rf) {
	
	rf.ui = rf.ui || {};

        rf.ui.SelectList =  function(id, listener, options) {
        	$super.constructor.call(this, id, listener, options);
        };
        
        rf.ui.PopupList.extend(rf.ui.SelectList);
    	var $super = rf.ui.SelectList.$super;
        
    	$.extend(rf.ui.SelectList.prototype,(function () {

    		return{
    			name : "selectList", 
    		}
    		
    	})());

})(jQuery, window.RichFaces);