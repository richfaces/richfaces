if (!window.Richfaces) {
	window.Richfaces = {};
}

Richfaces.jsFormSubmit = function(linkId, formName, target, params) {
	var form = document.getElementById(formName);
	if (form) {
		var formTarget = form.target;
		var paramNames = new Array();
		if (params) {
			for (var n in params) {
				paramNames.push(n);
			}
		}
	
		_JSFFormSubmit(linkId,formName,target,params);
		_clearJSFFormParameters(formName, formTarget, paramNames);
	}
}