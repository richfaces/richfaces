if (!window.RF_RW_DEMO) {
	window.RF_RW_DEMO = {};
}

(function(demo) {
	var findButtons = function(elt) {
		return $(elt).select('img');
	};
	
	demo.toPressed = function(elt) {
		var buttons = findButtons(elt);
		buttons[1].show();
		buttons[0].hide();
	};
	
	demo.toReleased = function(elt) {
		var buttons = findButtons(elt);
		buttons[0].show();
		buttons[1].hide();
	};
}(RF_RW_DEMO));
