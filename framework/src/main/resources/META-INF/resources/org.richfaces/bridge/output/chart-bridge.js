(function($, rf) {

  $.widget('rf.chartBridge', $.rf.bridgeBase, {

    _create : function() {
      this._super();

      var clientId = this.element.attr('id');

      $(document.getElementById(clientId + 'Chart')).chart(this.options);
      

      this._registerListeners();
     
    },
    
    _plotClickServerSide: function (event,clientId) {
    	var params = {};
        params[clientId + 'name'] = "plotclick";
        params[clientId + 'seriesIndex'] = event.data.seriesIndex;
        params[clientId + 'dataIndex'] = event.data.dataIndex;
        params[clientId + 'x'] = event.data.x;
        params[clientId + 'y'] = event.data.y;
        
        rf.ajax(clientId, event, {
      	  parameters : params,
      	  incId : 1
        });
    	
    },
    
    //bind listeners
    _registerListeners:function(){
        
        this.element.on('plotclick',this._getPlotClickHandler(this.options,this.element,this._plotClickServerSide));
        this.element.on('plothover',this._getPlotHoverHandler(this.options,this.element));
        if(this.options.handlers && this.options.handlers.onmouseout){
            this.element.on('mouseout',this.options.handlers.onmouseout);
        }
    },
    
  //function handles plotclick event. it calls server-side, client-side and particular series handlers if set.
    _getPlotClickHandler:function(options,element,serverSideHandler){
        return function(event,mouse,item){
            if(item !== null){
                //point in a chart clicked
                event.data={
                    seriesIndex: item.seriesIndex,
                    dataIndex:   item.dataIndex,
                    x: item.datapoint[0],
                    y: item.datapoint[1],
                    item:item
                };

                var clientId = element.attr('id');
                
                //sent request only if a server-side listener attached
                if(options.serverSideListener){
	                //server-side
	                if(serverSideHandler){
	                    serverSideHandler(event,clientId);
	                }
                }

                //client-side
                if(options.handlers && options.handlers['onplotclick']){
                    options.handlers['onplotclick'].call(element,event);
                }
                //client-side particular series handler
                if(options.particularSeriesHandlers && options.particularSeriesHandlers['onplotclick'][event.data.seriesIndex]){
                    options.particularSeriesHandlers['onplotclick'][event.data.seriesIndex].call(element,event);
                }
            }
        };
    },
    
  //function handles plothover event. it calls client-side and particular series handlers if set.
    _getPlotHoverHandler: function(options,element){
        return function(event,mouse,item){
            if(item !== null){
                //point in a chart clicked
                event.data={
                    seriesIndex: item.seriesIndex,
                    dataIndex:   item.dataIndex,
                    x: item.datapoint[0],
                    y: item.datapoint[1],
                    item:item
                };

                //client-side
                if(options.handlers && options.handlers['onplothover']){
                    options.handlers['onplothover'].call(element,event);
                }

                //client-side particular series handler
                if(options.particularSeriesHandlers && options.particularSeriesHandlers['onplothover'][event.data.seriesIndex]){
                    options.particularSeriesHandlers['onplothover'][event.data.seriesIndex].call(element,event);
                }
            }
        };
    },
    
    _unbind : function(){
    	this.element.off('plotclick');
    	this.element.off('plothover');
    	this.element.off('mouseout');
    },
    
    _destroy: function(){
      this._unbind();
    }
    
    
  });

}(RichFaces.jQuery, RichFaces));