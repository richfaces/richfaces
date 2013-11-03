(function($, rf) {

  $.widget('rf.chartBridge', $.rf.bridgeBase, {

    _create : function() {
      this._super();

      var clientId = this.element.attr('id');
      var bridge = this;

      this.chartOptions = $.extend({}, this.options, {

        
        
        plotClickServerSide : function(event) {
          
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
        
        }
      });

      $(document.getElementById(clientId + 'Chart')).chart(this.chartOptions);
      
      //bind handlers
      this._registerListeners();
     
    },
    
  //bind listeners
    _registerListeners:function(){
        
        this.element.on('plotclick',this._getPlotClickHandler(this.chartOptions,this.element));
        this.element.on('plothover',this._getPlotHoverHandler(this.chartOptions,this.element));
        if(this.chartOptions.handlers && this.chartOptions.handlers.onmouseout){
            this.element.on('mouseout',this.chartOptions.handlers.onmouseout);
        }
    },
    
  //function handles plotclick event. it calls server-side, client-side and particular series handlers if set.
    _getPlotClickHandler:function(options,element){
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

                //server-side
                if(options.plotClickServerSide){
                    options.plotClickServerSide(event);
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