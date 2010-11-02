RichFaces.csv.addValidator({"length":
(function(rf) {
	return function (value, params) {
		var result = "";
		if (value.length<params.min) {
			result = rf.csv.getMessage(params.customMessages, 'LengthValidator.MINIMUM', [params.min,value]);	
		} else if (value.length>params.max){
			result = rf.csv.getMessage(params.customMessages, 'LengthValidator.MAXIMUM', [params.max,value]);	
		}
		return result;
	}
})(window.RichFaces || (window.RichFaces={}))
});