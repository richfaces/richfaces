window.charttestutil = {

	pointX : function(id, seriesIndex, pointIndex) {
		return RichFaces.component(id).options.data[seriesIndex].data[pointIndex][0];
	},

	pointY : function(id, seriesIndex, pointIndex) {
		return RichFaces.component(id).options.data[seriesIndex].data[pointIndex][1];
	},

	pointXPos : function(id, seriesIndex, pointIndex) {
		// line chart only for now
		var xVal = window.charttestutil.pointX(id, seriesIndex, pointIndex);
		var plotObj = RichFaces.component(id).getPlotObject();
		return plotObj.getXAxes()[0].p2c(xVal) + plotObj.offset().left;
	},

	pointYPos : function(id, seriesIndex, pointIndex) {
		// line chart only for now
		var yVal = window.charttestutil.pointY(id, seriesIndex, pointIndex);
		var plotObj = RichFaces.component(id).getPlotObject();
		return plotObj.getYAxes()[0].p2c(yVal) + plotObj.offset().top - 25;// -10 originally
	}

}