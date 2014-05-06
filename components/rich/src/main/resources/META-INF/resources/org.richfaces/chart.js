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

    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};

    rf.rf4.ui.Chart = rf.BaseComponent.extendClass({
            // class name
            name:"Chart",

            init : function (componentId, options) {
            	options = $.extend(true,defaultOptions,options);
            	
            	this.element = $(document.getElementById(componentId));
            	
            	if (options.charttype === 'pie') {
                    options = $.extend(true,options, pieDefaults);
                    //transform data from
                    // [[{data:1, label:"label1"},{data:2,label:"label2"},...]]
                    //to
                    // [{data:1, label:"label1"},{data:2,label:"label2"},...]
                    options.data = options.data[0]; //pie chart data should not be in a collection
                }
            	$super.constructor.call(this, componentId, options);
                $.plot(this.element,options.data,options);
            },

            /***************************** Public Methods  ****************************************************************/

            

            /***************************** Private Methods ********************************************************/


            destroy: function () {
                rf.Event.unbindById(this.id, "." + this.namespace);
                $super.destroy.call(this);
            }
        });

    // define super class link
    var $super = rf.rf4.ui.Chart.$super;
})(RichFaces.jQuery, RichFaces);
