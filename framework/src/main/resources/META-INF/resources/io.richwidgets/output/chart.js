/**
 * A widget for creating data chart
 *
 * @module Output
 * @class chart
 */
(function( $ ) {

    $.widget( 'rf.chart', {

        // These options will be used as defaults for all chart types
        options: {
            grid:{
                clickable:true,
                hoverable:true
            },
            tooltip:true,
            tooltipOpts: {
                content: '%s  [%x,%y]',
                shifts: {
                    x: 20,
                    y: 0
                },
                defaultTheme: false
            }
        },

        // Default options for pie chart
        pieDefaults:{
            series:{
                pie:{
                    show:true
                }
            },
            tooltipOpts: {
                content: ' %p.0%, %s',
                shifts: {
                    x: 20,
                    y: 0
                },
                defaultTheme: false
            }
        },

        //Default options charts with a date data type on xaxis
        dateDefaults:{
            xaxis:{
                mode:'time',
                timeformat:'%Y/%m/%d',
                minTickSize:[1,'day']  //TODO
            }
        },

        // Set up the widget
        _create: function() {
            this._handleTypeDependentOptions();
            this._draw();
            this._registerListeners();
        },

        //if chart is created by JSF component and charttype option is set, there is need to update some options.
        _handleTypeDependentOptions:function(){

            if(this.options.charttype==='pie'){
                this.options = $.extend(this.options,this.pieDefaults);
                //transform data from
                // [[{data:1, label:"label1"},{data:2,label:"label2"},...]]
                //to
                // [{data:1, label:"label1"},{data:2,label:"label2"},...]
                this.options.data = this.options.data[0]; //pie chart data should not be in a collection
            }
            else if(this.options.charttype==='bar'){
                if(this.options.xtype==='string' || this.options.xtype==='class java.lang.String'){
                    //category bar chart
                    /*transformation data from
                     [
                        {   data:   {"Key1":11, "Key2":21, "Key3": 31},
                            label:  "label1",
                            bars:   {show:true}
                        },

                        {   data:   {"Key1":12,"Key2":22, "Key3": 32},
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
                            -missing consider to be 0   (ie. "Key3" in fourth series)
                            -ignore additional          (ie. "Key5" in fourth series)
                     keys:
                         COUNTER=0
                         - for each key used in the first series
                               add couple [COUNTER, KEY_LABEL] to ticks
                               COUNTER++
                     order:
                         order of the series





                     */


                    var ticks=[],keys=[],first=true,order=0;

                    for (var index in this.options.data){//loop through data series
                        var convertedData=[];
                        var cnt=0;
                        if(first){//the first series determine which keys (x-values are plotted)
                            for( var key in this.options.data[index].data){
                                ticks.push([cnt,key]);
                                keys.push(key);
                                convertedData.push([cnt,this.options.data[index].data[key]]);
                                cnt++;
                            }
                            first=false;
                        }
                        else{
                            for (var k in keys){ //select values for first series keys only
                                var loopKey=keys[k];
                                if(this.options.data[index].data[loopKey]){
                                    convertedData.push([cnt,this.options.data[index].data[loopKey]]);
                                }
                                else{
                                    convertedData.push([cnt,0]);
                                }
                                cnt++;
                            }
                        }
                        this.options.data[index].data=convertedData;
                        var bars={
                            order:order,
                            show:true
                        };
                        this.options.data[index].bars=bars;
                        order++;

                    }

                    //add ticks to the options
                    this.options.xaxis= $.extend(this.options.xaxis,{
                        ticks:ticks,
                        tickLength:0,
                        //workaround to show display proper x-value on category bars
                        tickFormatter:function(value,axis){return axis.ticks[value].label;}
                    });

                    //options for all bars
                    this.options.bars= $.extend(this.options.bars,{
                        show: true,
                        barWidth: 0.2,
                        align:'center'
                    });
                }
            }
            else if(this.options.charttype==='line'){
                if(this.options.zoom){
                    this.options.selection={mode: 'xy'};
                }
                if(this.options.xtype==='date' || this.options.xtype==='class java.util.Date'){
                    this.options = $.extend(this.options,this.dateDefaults);
                    if(this.options.xaxis.format){
                        this.options.xaxis.timeformat=this.options.xaxis.format;
                    }
                }
            }
        },


        // Use the _setOption method to respond to changes to options
        _setOption: function( key, value ) {
            // In jQuery UI 1.8, you have to manually invoke the _setOption method from the base widget
            $.Widget.prototype._setOption.apply( this, arguments );
            // In jQuery UI 1.9 and above, you use the _super method instead
            this._super( '_setOption', key, value );

            var redraw=true; //variable decides whether redraw of the chart is required

            //the change of a handler does not require chart to be redrawn
            switch( key ) {
                case 'zoom':
                case 'handlers':
                case 'particularSeriesHandlers':
                    this._unbind();
                    this._registerListeners();
                    redraw=false;
                    break;
            }

            if(redraw){
                this._draw();
            }
        },

        //method draws a chart if opts given it is used as options for the chart. Otherwise this.options is used
        _draw:function(opts){
            if(opts){
                this.plot = $.plot(this.element,opts.data,opts);
            }
            else{
                this.plot = $.plot(this.element,this.options.data,this.options);
            }
        },

        //bind listeners
        _registerListeners:function(){
            if(this.options.zoom){
                this.element.on('plotselected',this._getZoomFunction(this,this.element,this.options));
            }
            this.element.on('plotclick',this._getPlotClickHandler(this.options,this.element));
            this.element.on('plothover',this._getPlotHoverHandler(this.options,this.element));
            if(this.options.handlers && this.options.handlers.onmouseout){
                this.element.on('mouseout',this.options.handlers.onmouseout);
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
                    if(options.handlers && options.handlers.eventFunction){
                        options.handlers.eventFunction(event,'plotclick',
                            event.data.seriesIndex,
                            event.data.dataIndex,
                            event.data.x,
                            event.data.y);
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


        //function handling zooming
        _getZoomFunction : function(widget,element,options){
            return function(event,ranges){
                // clamp the zooming to prevent eternal zoom

                if (ranges.xaxis.to - ranges.xaxis.from < 0.00001) {
                    ranges.xaxis.to = ranges.xaxis.from + 0.00001;
                }

                if (ranges.yaxis.to - ranges.yaxis.from < 0.00001) {
                    ranges.yaxis.to = ranges.yaxis.from + 0.00001;
                }

                // do the zooming

                widget._draw($.extend({}, widget.options, {
                    xaxis: { min: ranges.xaxis.from, max: ranges.xaxis.to },
                    yaxis: { min: ranges.yaxis.from, max: ranges.yaxis.to }
                })
                );
            };
        },

        /**
         * Redraw chart without axis ranges
         *
         * @method resetZoom
         */
        resetZoom: function(){
            this._draw();
        },


        /**
         * Returns chart object
         *
         * @method getPlotObject
         */
        getPlotObject: function(){
            return this.plot;
        },

        //remove chart handler bindings
        _unbind:function(){
            this.element.off('plotclick');
            this.element.off('plothover');
            this.element.off('plotselected');
            this.element.off('mouseout');
        },

        /**
         * Use the destroy method to clean up any modifications your widget has made to the DOM
         *
         * @method destroy
         */
        destroy: function() {
            this.plot.shutDown();
            this._unbind();
            // In jQuery UI 1.8, you must invoke the destroy method from the base widget
            $.Widget.prototype.destroy.call( this );
            // In jQuery UI 1.9 and above, you would define _destroy instead of destroy and not call the base method
        }
    });
}( jQuery ) );
