(function ($, rf) {
	
	rf.ui = rf.ui || {};

        rf.ui.SelectList =  function(id, listener, options) {
        	$super.constructor.call(this, id, listener, options);
        	this.selectFirst = options.selectFirst;
        };
        
        rf.ui.PopupList.extend(rf.ui.SelectList);
    	var $super = rf.ui.SelectList.$super;
    	
    	$.extend(rf.ui.SelectList.prototype,(function () {

    		return{
    			name : "selectList", 
    			
    			show: function() {
    				if(!this.isVisible()) {
    					$super.show.call(this);
    					if(this.selectFirst) {
    						this.__selectByIndex(0);
    					}
    				}
    			}, 
    			
    			hide: function() {
    				if(this.isVisible()) {
    					$super.hide.call(this);
    					this.__selectByIndex(-1);
    				}	
    			}
    		}
    		
    	})());

})(jQuery, window.RichFaces);