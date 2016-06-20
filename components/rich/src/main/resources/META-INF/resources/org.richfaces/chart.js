/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

(function ($, rf) {
	var defaultOptions = {

	      /**
	       * Specifies the type of chart.
	       * According to this property and xtypex ytype, proper default options corresponding to chart type
	       * are applied and data are transformed to the format expected by flot.
	       * Following values are supported: `line`,`bar`,`pie`
	       * Options is not required. If it is not used the options and data format remains the same as flot uses by default.
	       * @property charttype
	       * @default null
	       */
	      charttype: '',

	      /**
	       * Specify the data type of values plotted on x-axis
	       * Following options are supported: number,string,date
	       * @property xtype
	       * @default null
	       */
	      xtype: '',

	      /**
	       * Specify the data type of values plotted on x-axis
	       * Following options are supported: number,string,date
	       * @property xtype
	       * @default null
	       */
	      ytype: '',

	      /**
	       * Allows to zoom the chart. Supported only when line chart is used. Requires charttype to be set.
	       * @property zoom
	       * @default false
	       */
	      zoom: false,



	     /**
	      * Options customizing the chart grid.
	      * @type Object
	      * @default {clickable:true, hoverable:true}
	      * @property grid
	      *   @property clickable {boolean}
	      *   Grid accepts click events.
	      *   @property hoverable {boolean}
	      *   Grid fires mouseover event. Necessary for tooltip to work.
	      *
	      */
	     grid: {
	        clickable: true,
	        hoverable: true
	      },

	      /**
	       * Turns on tooltip (text shown when mouse points to a part of a chart)
	       * @property tooltip
	       * @type boolean
	       * @default true
	       */
	      tooltip: true,

	      /**
	       * Customizes the tooltip.
	       * @type Object
	       * @default { content: '%s [%x,%y]'}
	       * @property tooltipOpts
	       *    @property content {string}
	       *    Specify the tooltip format. Use %s for series label, %x for X values, %y for Y value
	       *    @property defaultTheme
	       */
	      tooltipOpts: {
	        content: '%s  [%x,%y]',
	        shifts: {
	          x: 20,
	          y: 0
	        },
	        defaultTheme: false
	      },

	      /**
	       * Legend properties
	       * @type Object
	       * @default {postion:'ne', sorted: 'ascending'}
	       * @property legend
	       *    @property position {string}
	       *    Defines the placement of the legend in the grid. One of ne,nw,se,sw
	       *    @property sorted {string}
	       *    Defines the order of labels in the legend. One of ascending,descending,false.
	       */
	      legend: {
	        postion:'ne',
	        sorted: 'ascending'
	      },

	      /**
	       * Customizes the horizontal axis
	       * @type Object
	       * @default {min: null, max: null,autoscaleMargin: null, axisLabel: ''}
	       * @property xaxis
	       *   @property min {number}
	       *   Minimal value shown on axis
	       *   @property max {number}
	       *   Maximal values show on axis
	       *   @property autoscaleMargin {number}
	       *   It's the fraction of margin that the scaling algorithm will add
	       *   to avoid that the outermost points ends up on the grid border
	       *   @property axisLabel {string}
	       *   Axis description
	       */
	      xaxis:{
	        min: null,
	        max: null,
	        autoscaleMargin: null,
	        axisLabel: ''
	      },

	      /**
	       * Customizes the vertical axis
	       * @type Object
	       * @default {min: null, max: null,autoscaleMargin: 0.2,axisLabel: ''}
	       * @property yaxis
	       *   @property min {number}
	       *   Minimal value shown on axis
	       *   @property max {number}
	       *   Maximal values show on axis
	       *   @property autoscaleMargin {number}
	       *   It's the fraction of margin that the scaling algorithm will add to
	       *   avoid that the outermost points ends up on the grid border.
	       *   @property axisLabel {string}
	       *   Axis description
	       */
	      yaxis:{
	        min: null,
	        max: null,
	        autoscaleMargin: 0.2,
	        axisLabel: ''
	      },




	      /**
	       * Data to be plotted. The format is the same used by flot. The format may differ if the charttype
	       * option is set to pie or bar.
	       * @property data
	       * @default []
	       */
	      data:[]

	    };
	    
	var pieDefaults = {
	      series: {
	          pie: {
	            show: true
	          }
	        },
	        tooltipOpts: {
	          content: ' %p.0%, %s'
	        }
	      };
	 var _plotClickServerSide = function (event, clientId) {
	      var params = {};
	      params[clientId + 'name'] = "plotclick";
	      params[clientId + 'seriesIndex'] = event.data.seriesIndex;
	      params[clientId + 'dataIndex'] = event.data.dataIndex;
	      params[clientId + 'x'] = event.data.x;
	      params[clientId + 'y'] = event.data.y;

	      rf.ajax(clientId, event, {
	        parameters: params,
	        incId: 1
	      });

	    };

        var handleStringTicks = function(options) {
        /*
         * data transformation:
         * data: [["label1", value1], ["label2", value2"], …]
         * 
         * =>
         * 
         * data: [[0, value1], [1, value2], …] 
         * ticks: [[0, "label1"], [1, "label2"], …]
         *
         */
        options.xaxis.tickLength = 0;

        var seriesLength,
            seriesTotal = options.data.length,
            _tickNames = {},
            _tickTotal = 0,
            _currTickNumber,
            _currItem;

        if (options.charttype === 'bar') {
            options.bars = options.bars || {};
            options.bars.barWidth = 1 / (seriesTotal + 1);
        }
        for (var i = 0; i < seriesTotal; i++) {
            seriesLength = options.data[i].data.length;

            if (options.charttype === 'bar') {
                options.data[i].bars.order = i;
            }
            for (var j = 0; j < seriesLength; j++) { 
                _currItem = options.data[i].data[j];
                _currTickNumber = _tickNames[_currItem[0]];

                if (_currTickNumber == undefined) {
                    _currTickNumber = _tickTotal++;
                    _tickNames[_currItem[0]] = _currTickNumber;
                }

                _currItem[0] = _currTickNumber;
            }
        }

        options.xaxis.ticks = [];
        for (tick in _tickNames) {
            options.xaxis.ticks.push([_tickNames[tick],tick]);
        }
    };

    rf.ui = rf.ui || {};

    rf.ui.Chart = rf.BaseComponent.extendClass({
            // class name
            name:"Chart",

            /**
             * Backing object for rich:chart
             * 
             * @extends RichFaces.BaseComponent
             * @memberOf! RichFaces.ui
             * @constructs RichFaces.ui.Chart
             * 
             * @param componentId
             * @param options
             */
            init : function (componentId, options) {
            	$super.constructor.call(this, componentId, options);

                this.namespace = this.namespace || "." + RichFaces.Event.createNamespace(this.name, this.id);
                this.attachToDom();
            	this.options = $.extend(true,{},defaultOptions,options);
            	
            	this.element = $(document.getElementById(componentId));
                this.chartElement = this.element.find(".chart");
            	
            	if (this.options.charttype === 'pie') {
                    this.options = $.extend(true,{},this.options,pieDefaults);
                    //transform data from
                    // [[{data:1, label:"label1"},{data:2,label:"label2"},...]]
                    //to
                    // [{data:1, label:"label1"},{data:2,label:"label2"},...]
                    this.options.data = this.options.data[0]; //pie chart data should not be in a collection
                }
                else if (this.options.charttype === 'bar') {
                    if (this.options.xtype === 'string') {
                        handleStringTicks(this.options);
                    }
                  }
                  else if (options.charttype === 'line') {
                    if (this.options.xtype === 'string') {
                        handleStringTicks(this.options);
                    }
                    if (options.zoom) {
                      this.options.selection = {mode: 'xy'};
                    }
                    if (this.options.xtype === 'date') {
                      this.options = $.extend({},this.options, dateDefaults);
                      if (this.options.xaxis.format) {
                        this.options.xaxis.timeformat = this.options.xaxis.format;
                      }
                    }
                  }
            	
                this.plot = $.plot(this.chartElement,this.options.data,this.options);
                this.__bindEventHandlers(this.chartElement,this.options);
            },

            /***************************** Public Methods  ****************************************************************/

            /**
             * Returns chart object
             *
             * @method
             * @name RichFaces.ui.Chart#getPlotObject
             * @return {Plot} chart object
             */
            getPlotObject: function () {
              return this.plot;
            },

            /**
             * Highlights the point in chart selected by seriesIndex or point index. Does not work for pie charts.
             * 
             * @method
             * @name RichFaces.ui.Chart#highlight
             * @param seriesIndex {int} index of the series
             * @param pointIndex {int} index of the point
             */
            highlight: function(seriesIndex,pointIndex){
               this.plot.highlight(seriesIndex,pointIndex);
            },
            /**
             * Removes highlighting of point. If method is called without parameters it unhighlights all points.
             * 
             * @method
             * @name RichFaces.ui.Chart#unhighlight
             * @param seriesIndex {int} index of the series
             * @param pointIndex {int} index of the point
             */
            unhighlight: function(seriesIndex,pointIndex){
               this.plot.unhighlight(seriesIndex,pointIndex);
            },

            /***************************** Private Methods ********************************************************/
            __bindEventHandlers:function(element,options){
            	
                element.on('plotclick', this._getPlotClickHandler(options, element.get(0), _plotClickServerSide));
                element.on('plothover', this._getPlotHoverHandler(options, element.get(0)));
            	if (options.handlers && options.handlers.onmouseout) {
                    element.on('mouseout', options.handlers.onmouseout);
                }

                if (options.zoom) {
                    element.on('plotselected', $.proxy(this._zoomFunction, this));
                }
                
            },
          //function handles plotclick event. it calls server-side, client-side and particular series handlers if set.
            _getPlotClickHandler: function (options, element, serverSideHandler) {
            	
              var clickHandler = options.handlers['onplotclick'];	
              var particularClickHandlers= options.particularSeriesHandlers['onplotclick'];
              var clientId = this.element.attr('id');
              return function (event, pos, item) {
                if (item !== null) {
                  //point in a chart clicked
                  event.data = {
                    seriesIndex: item.seriesIndex,
                    dataIndex: item.dataIndex,
                    x: item.datapoint[0],
                    y: item.datapoint[1],
                    item: item
                  };
                  if(options.charttype=="pie"){
                	  event.data.x = options.data[item.seriesIndex].label;
                	  event.data.y = item.datapoint[1][0][1];
                  }
                  else if(options.charttype=="bar" && options.xtype=="string"){
                    event.data.x = options.xaxis.ticks[item.dataIndex][1];

                  }
                } else {
                    event.data = {
                        x: pos.x,
                        y: pos.y
                    };
                }
                  //sent request only if a server-side listener attached
                  if (options.serverSideListener) {
                    //server-side
                    if (serverSideHandler) {
                      serverSideHandler(event, clientId);
                    }
                  }

                  //client-side
                  if (clickHandler) {
                	 clickHandler.call(element, event);
                  }
                  //client-side particular series handler
                  if (particularClickHandlers[event.data.seriesIndex]) {
                	  particularClickHandlers[event.data.seriesIndex].call(element, event);
                  }
              };
            },
            
            //function handles plothover event. it calls client-side and particular series handlers if set.
            _getPlotHoverHandler: function (options, element) {
              var hoverHandler = options.handlers['onplothover'];	
              var particularHoverHandlers =	options.particularSeriesHandlers['onplothover']; 
              
              return function (event, pos, item) {
                if (item !== null) {
                  //point in a chart clicked
                  event.data = {
                    seriesIndex: item.seriesIndex,
                    dataIndex: item.dataIndex,
                    x: item.datapoint[0],
                    y: item.datapoint[1],
                    item: item
                  };
                } else {
                    event.data = {
                        x: pos.x,
                        y: pos.y
                    };
                }
                  //client-side
                  if (hoverHandler) {
                    hoverHandler.call(element, event);
                  }

                  //client-side particular series handler
                  if (particularHoverHandlers[event.data.seriesIndex]) {
                	  particularHoverHandlers[event.data.seriesIndex].call(element, event);
                  }
                
              };
            },

            _zoomFunction: function (event, ranges) {
                var plot = this.getPlotObject();
                $.each(plot.getXAxes(), function(_, axis) {
                    var opts = axis.options;
                    opts.min = ranges.xaxis.from;
                    opts.max = ranges.xaxis.to;
                });
                plot.setupGrid();
                plot.draw();
                plot.clearSelection();
            },

            /**
             * Reset the zoom level
             *
             * @method
             * @name RichFaces.ui.Chart#resetZoom
             */
            resetZoom: function () {
                this.plot = $.plot(this.chartElement,this.options.data,this.options);
            },

            destroy: function () {
                rf.Event.unbindById(this.id, "." + this.namespace);
                $super.destroy.call(this);
            }
        });

    // define super class link
    var $super = rf.ui.Chart.$super;
})(RichFaces.jQuery, RichFaces);
