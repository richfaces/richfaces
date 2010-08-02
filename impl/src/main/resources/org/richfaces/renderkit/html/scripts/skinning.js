(function(jQuery) {
	var userAgent = navigator.userAgent;
	var skipNavigator = window.opera || (userAgent.indexOf('AppleWebKit/') > -1 && userAgent.indexOf('Chrome/') == -1);
	if (!skipNavigator) {
		var f = function() {		
			jQuery("head > link[media='rich-extended-skinning']").removeAttr('media');
		};
		jQuery(document).ready(f);
		f();
	}
}(jQuery));