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
	       *    @property content {String}
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
	       *    @property position {String}
	       *    Defines the placement of the legend in the grid. One of ne,nw,se,sw
	       *    @property sorted {String}
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
	       *   @property min {Number}
	       *   Minimal value shown on axis
	       *   @property max {Number}
	       *   Maximal values show on axis
	       *   @property autoscaleMargin {Number}
	       *   It's the fraction of margin that the scaling algorithm will add
	       *   to avoid that the outermost points ends up on the grid border
	       *   @property axisLabel {String}
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
	       *   @property min {Number}
	       *   Minimal value shown on axis
	       *   @property max {Number}
	       *   Maximal values show on axis
	       *   @property autoscaleMargin {Number}
	       *   It's the fraction of margin that the scaling algorithm will add to
	       *   avoid that the outermost points ends up on the grid border.
	       *   @property axisLabel {String}
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

    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};

    rf.rf4.ui.Chart = rf.BaseComponent.extendClass({
            // class name
            name:"Chart",

            init : function (componentId, options) {
            	options = $.extend(true,{},defaultOptions,options);
            	
            	this.element = $(document.getElementById(componentId));
            	
            	if (options.charttype === 'pie') {
                    options = $.extend(true,{}, options,pieDefaults);
                    //transform data from
                    // [[{data:1, label:"label1"},{data:2,label:"label2"},...]]
                    //to
                    // [{data:1, label:"label1"},{data:2,label:"label2"},...]
                    options.data = options.data[0]; //pie chart data should not be in a collection
                }
            	else if (options.charttype === 'bar') {
                    if (options.xtype === 'string') {
                      //category bar chart
                      /*transformation data from
                       [
                         {  data:   {"Key1":11, "Key2":21, "Key3": 31},
                            label:  "label1",
                            bars:   {show:true}
                         },

                         {  data:   {"Key1":12,"Key2":22, "Key3": 32},
                            label:  "label2",
                            bars:   {show:true}
                         },

                         {   data:   {"Key1":13,"Key2":23,"Key3":33},
                            label:  "label3",
                            bars:   {show:true}
                         },
                         {   data:   {"Key1":14,"Key2":24,"Key4":44},
                           label:  "label4",
                           bars:   {show:true}
                         }
                       ]

                       to the form:


                       [
                         {
                           data:   [
                              [0,11],[1,21],[2, 31]
                           ],
                           label:  "label1",
                           bars: {
                             show:true,
                             order=0
                           }
                         },
                         {
                           data:   [
                              [0,12],[1,22],[2, 32]
                           ],
                           label:  "label2",
                           bars: {
                             show:true,
                             order=1
                           }
                         },
                         {
                           data:   [
                              [0,13],[1,23],[2, 33]
                           ],
                           label:  "label3",
                           bars: {
                               show:true,
                               order=2
                           }
                         },
                         {
                           data:   [
                              [0,14],[1,24],[2, 0]
                           ],
                           label:  "label4",
                           bars: {
                             show:true,
                             order=3
                           }
                         },
                       ]

                       and create array ticks

                       ticks = [[0,"Key1"],[1,"Key2"],[2,"Key3"]] and assign this array to the this.options.xaxis


                       according to the following rules:
                       data:
                         - select all keys used in the first series (set)
                         - in the following series find the keys selected in first
                         - missing consider to be 0   (ie. "Key3" in fourth series)
                         - ignore additional          (ie. "Key5" in fourth series)
                       keys:
                         COUNTER=0
                          - for each key used in the first series
                             add couple [COUNTER, KEY_LABEL] to ticks
                             COUNTER++
                       order:
                          order of the series
                      */


                      var ticks = [], keys = [], first = true, order = 0;

                      /**
                       * Labels are mapped to numbers 0,1,2...
                       * If a chart consists of multiple series values(from all series) with the same label
                       * are mapped to the (one number 0,1,2..) space of width 1.
                       * barWidth takes care of bars to not overlap
                       * @type {number}
                       */
                      var barWidth =  1 / (options.data.length + 1);

                      for (var index in options.data) {//loop through data series
                        var convertedData = [];
                        var cnt = 0;
                        if (first) {//the first series determine which keys (x-values are plotted)
                          for (var key in options.data[index].data) {
                            ticks.push([cnt, key]);
                            keys.push(key);
                            convertedData.push([cnt, options.data[index].data[key]]);
                            cnt++;
                          }
                          first = false;
                        }
                        else {
                          for (var k in keys) { //select values for first series keys only
                            var loopKey = keys[k];
                            if (options.data[index].data[loopKey]) {
                              convertedData.push([cnt,options.data[index].data[loopKey]]);
                            }
                            else {
                              convertedData.push([cnt, 0]);
                            }
                            cnt++;
                          }
                        }
                        options.data[index].data = convertedData;
                        var bars = {
                          order: order,
                          show: true
                        };
                        options.data[index].bars = bars;
                        order++;

                      }

                      //add ticks to the options
                      options.xaxis = $.extend({},options.xaxis, {
                        ticks: ticks,
                        tickLength: 0,
                        //workaround to show display proper x-value on category bars
                        tickFormatter: function (value, axis) {
                          return axis.ticks[value].label;
                        }
                      });


                      //options for all bars
                      options.bars = $.extend(options.bars, {
                        show: true,
                        barWidth: barWidth,
                        align: 'center'
                      });
                    }
                  }
                  else if (options.charttype === 'line') {
                    if (options.zoom) {
                      options.selection = {mode: 'xy'};
                    }
                    if (options.xtype === 'date') {
                      options = $.extend(options, dateDefaults);
                      if (options.xaxis.format) {
                        options.xaxis.timeformat = options.xaxis.format;
                      }
                    }
                  }
            	$super.constructor.call(this, componentId, options);
                $.plot(this.element,options.data,options);
                this.__bindEventHandlers(this.element,options);
            },

            /***************************** Public Methods  ****************************************************************/

            

            /***************************** Private Methods ********************************************************/
            __bindEventHandlers:function(element,options){
            	element.on('plotclick', this._getPlotClickHandler(options, element, _plotClickServerSide));
            	element.on('plothover', this._getPlotHoverHandler(options, element));
            	if (options.handlers && options.handlers.onmouseout) {
                    element.on('mouseout', options.handlers.onmouseout);
                 }
                console.log("Bindujem handlery"); 
            },
          //function handles plotclick event. it calls server-side, client-side and particular series handlers if set.
            _getPlotClickHandler: function (options, element, serverSideHandler) {
              return function (event, mouse, item) {
                if (item !== null) {
                  //point in a chart clicked
                  event.data = {
                    seriesIndex: item.seriesIndex,
                    dataIndex: item.dataIndex,
                    x: item.datapoint[0],
                    y: item.datapoint[1],
                    item: item
                  };

                  var clientId = element.attr('id');

                  //sent request only if a server-side listener attached
                  //if (options.serverSideListener) {
                    //server-side
                    if (serverSideHandler) {
                      serverSideHandler(event, clientId);
                    }
                  //}

                  //client-side
                  if (options.handlers && options.handlers['onplotclick']) {
                    options.handlers['onplotclick'].call(element, event);
                  }
                  //client-side particular series handler
                  if (options.particularSeriesHandlers && options.particularSeriesHandlers['onplotclick'][event.data.seriesIndex]) {
                    options.particularSeriesHandlers['onplotclick'][event.data.seriesIndex].call(element, event);
                  }
                }
              };
            },
            
            //function handles plothover event. it calls client-side and particular series handlers if set.
            _getPlotHoverHandler: function (options, element) {
              return function (event, mouse, item) {
                if (item !== null) {
                  //point in a chart clicked
                  event.data = {
                    seriesIndex: item.seriesIndex,
                    dataIndex: item.dataIndex,
                    x: item.datapoint[0],
                    y: item.datapoint[1],
                    item: item
                  };

                  //client-side
                  if (options.handlers && options.handlers['onplothover']) {
                    options.handlers['onplothover'].call(element, event);
                  }

                  //client-side particular series handler
                  if (options.particularSeriesHandlers && options.particularSeriesHandlers['onplothover'][event.data.seriesIndex]) {
                    options.particularSeriesHandlers['onplothover'][event.data.seriesIndex].call(element, event);
                  }
                }
              };
            },

            destroy: function () {
                rf.Event.unbindById(this.id, "." + this.namespace);
                $super.destroy.call(this);
            }
        });

    // define super class link
    var $super = rf.rf4.ui.Chart.$super;
})(RichFaces.jQuery, RichFaces);
