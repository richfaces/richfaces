if (!window.RichFaces) {
	window.RichFaces = {};
}

RichFaces.MSIE		= 0;
RichFaces.FF		= 1;
RichFaces.OPERA		= 2;
RichFaces.NETSCAPE	= 3;
RichFaces.SAFARI	= 4;
RichFaces.KONQ		= 5;

RichFaces.navigatorType = function () {
	var userAgent = navigator.userAgent.toLowerCase();
	if (userAgent.indexOf("msie") >= 0 ||
			userAgent.indexOf("explorer") >= 0)
		return RichFaces.MSIE;
	if (userAgent.indexOf("firefox") >= 0 ||
			userAgent.indexOf("iceweasel") >= 0)
		return RichFaces.FF;
	if (userAgent.indexOf("opera") >= 0)
		return RichFaces.OPERA;
	if (userAgent.indexOf("netscape") >= 0)
		return RichFaces.NETSCAPE;
	if (userAgent.indexOf("safari") >= 0)
		return RichFaces.SAFARI;
	if (userAgent.indexOf("konqueror") >= 0)
		return RichFaces.KONQ;
	return "OTHER";
}

RichFaces.getOperaVersion = function () {
	var userAgent = navigator.userAgent.toLowerCase();
	var index = userAgent.indexOf("opera");
	if (index == -1) return;
	return parseFloat(userAgent.substring(index+6));
}

RichFaces.getIEVersion = function () {
	var searchString = "msie";
	var agent = navigator.userAgent.toLowerCase();
	var idx = agent.indexOf(searchString);
	if (idx != -1) {
		var versIdx = agent.indexOf(";", idx);
		var versString;

		if (versIdx != -1) {
			versString = agent.substring(idx + searchString.length, versIdx);
		} else {
			versString = agent.substring(idx + searchString.length);
		}
		
		return parseFloat(versString);
	} else {
		return undefined;
	}
}
