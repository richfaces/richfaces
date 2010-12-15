/*
 * code review by Pavel Yaschenko
 * 
 * records in draggables and droppables should be cleaned up when remove component from DOM
 * 
 * draft code: something like this
 * 
 *
    rf.ui.Draggable = function (id, options) {
	 	var c = rf.$(id);
		if (c) {
			var baseDestroy = c.destroy;
			c. destroy = f () 
				{
					rf.ui.DnDManager.removeDraggable(id)
					baseDestroy.call(this);
				}
		}
		// other code ...
	}
 */

(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
	rf.ui.DnDManager = rf.ui.DnDManager || {};  

	$.extend(rf.ui.DnDManager, ( function () {
		var draggables = {};
		var droppables = {};

		return {
    			draggable: function(event, id, options) {
					var draggable = draggables[id];
					if(!draggable) {
						options['event'] = event; 
						draggables[id] = new rf.ui.Draggable(id, options);
					}
				}, 
				
				droppable: function(event, id, options) {
					var droppable = droppables[id];
					if(!droppable) {
						options['event'] = event;
						droppables[id] = new rf.ui.Droppable(id, options);
					}
				}
			}
    	})());
})(jQuery, window.RichFaces);