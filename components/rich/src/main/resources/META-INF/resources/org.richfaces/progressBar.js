(function ($, rf) {

    rf.ui = rf.ui || {};

    var defaultOptions = {
        interval: 1000,
        minValue: 0,
        maxValue: 100
    };

    var stateSelectors = {
        initial: '> .rf-pb-init',
        progress: '> .rf-pb-rmng',
        finish: '> .rf-pb-fin'
    };

    // Constructor definition
    /**
     * Backing object for rich:progressBar
     * 
     * @extends RichFaces.BaseComponent
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.ProgressBar
     * 
     * @param componentId
     * @param options
     */
    rf.ui.ProgressBar = function(componentId, options) {
        // call constructor of parent class
        $super.constructor.call(this, componentId);
        this.__elt = this.attachToDom();
        this.options = $.extend(this.options, defaultOptions, options || {});
        this.enabled = this.options.enabled;
        this.minValue = this.options.minValue;
        this.maxValue = this.options.maxValue;

        this.__setValue(this.options.value || this.options.minValue /* TODO - check with Ilya */);

        if (this.options.resource) {
            this.__poll();
        } else if (this.options.submitFunction) {
            this.submitFunction = new Function("beforeUpdateHandler", "afterUpdateHandler", "params", "event", this.options.submitFunction);
            this.__poll();
        }

        if (this.options.onfinish) {
            rf.Event.bind(this.__elt, "finish", new Function("event", this.options.onfinish));
        }
    };

    // Extend component class and add protected methods from parent class to our container
    rf.BaseComponent.extend(rf.ui.ProgressBar);

    // define super class link
    var $super = rf.ui.ProgressBar.$super;

    $.extend(rf.ui.ProgressBar.prototype, (function() {
        return {
            name: "ProgressBar",

            __isInitialState: function() {
                return parseFloat(this.value) < parseFloat(this.getMinValue());
            },

            __isProgressState: function() {
                return !this.__isInitialState() && !this.__isFinishState();
            },

            __isFinishState: function() {
                return parseFloat(this.value) >= parseFloat(this.getMaxValue());
            },

            __beforeUpdate: function(event) {
                if (event.componentData && typeof event.componentData[this.id] != 'undefined') {
                    this.setValue(event.componentData[this.id]);
                }
            },

            __afterUpdate: function(event) {
                this.__poll();
            },

            __onResourceDataAvailable: function(data) {
                var parsedData = rf.parseJSON(data);
                if (parsedData instanceof Number || typeof parsedData == 'number') {
                    this.setValue(parsedData);
                }

                this.__poll();
            },

            __submit: function() {
                if (this.submitFunction) {
                    this.submitFunction.call(this, $.proxy(this.__beforeUpdate, this), $.proxy(this.__afterUpdate, this),
                        this.__params || {});
                } else {
                    $.get(this.options.resource, this.__params || {}, $.proxy(this.__onResourceDataAvailable, this), 'text');
                }
            },

            __poll: function(immediate) {
                if (this.enabled) {
                    if (immediate) {
                        this.__submit();
                    } else {
                        this.__pollTimer = setTimeout($.proxy(this.__submit, this), this.options.interval);
                    }
                }
            },

            __calculatePercent: function(v) {
                var min = parseFloat(this.getMinValue());
                var max = parseFloat(this.getMaxValue());
                var value = parseFloat(v);
                if (min < value && value < max) {
                    return (100 * (value - min)) / (max - min);
                } else if (value <= min) {
                    return 0;
                } else if (value >= max) {
                    return 100;
                }
            },

            __getPropertyOrObject: function(obj, propName) {
                if ($.isPlainObject(obj) && obj.propName) {
                    return obj.propName;
                }

                return obj;
            },

            /**
             * Get current value
             *
             * @method
             * @name RichFaces.ui.ProgressBar#getValue
             * @return {int} current value
             */
            getValue: function() {
                return this.value;
            },

            __showState: function (state) {
                var stateElt = $(stateSelectors[state], this.__elt);

                if (stateElt.length == 0 && (state == 'initial' || state == 'finish')) {
                    stateElt = $(stateSelectors['progress'], this.__elt)
                }

                stateElt.show().siblings().hide();
            },

            __setValue: function(val, initialStateSetup) {
                this.value = parseFloat(this.__getPropertyOrObject(val, "value"));

                if (this.__isFinishState() || this.__isInitialState()) {
                    this.disable();
                }
            },

            __updateVisualState: function() {
                if (this.__isInitialState()) {
                    this.__showState("initial");
                } else if (this.__isFinishState()) {
                    this.__showState("finish");
                } else {
                    this.__showState("progress");
                }

                var p = this.__calculatePercent(this.value);
                $(".rf-pb-prgs", this.__elt).css('width', p + "%");
            },

            /**
             * Set new value
             *
             * @method
             * @name RichFaces.ui.ProgressBar#setValue
             * @param value {int} new value
             */
            setValue: function(val) {
                var wasInFinishState = this.__isFinishState();

                this.__setValue(val);
                this.__updateVisualState();

                if (!wasInFinishState && this.__isFinishState()) {
                    rf.Event.callHandler(this.__elt, "finish");
                }
            },

            /**
             * Get max value of the progress bar
             *
             * @method
             * @name RichFaces.ui.ProgressBar#getMaxValue
             * @return {int} max value
             */
            getMaxValue: function() {
                return this.maxValue;
            },

            /**
             * Get min value of the progress bar
             *
             * @method
             * @name RichFaces.ui.ProgressBar#getMinValue
             * @return {int} min value
             */
            getMinValue: function() {
                return this.minValue;
            },

            isAjaxMode: function () {
                return !!this.submitFunction || !!this.options.resource;
            },

            /**
             * Disable the progress bar
             *
             * @method
             * @name RichFaces.ui.ProgressBar#disable
             */
            disable: function () {
                this.__params = null;
                if (this.__pollTimer) {
                    clearTimeout(this.__pollTimer);
                    this.__pollTimer = null;
                }

                this.enabled = false;
            },

            /**
             * Enable the progress bar
             *
             * @method
             * @name RichFaces.ui.ProgressBar#enable
             */
            enable: function (params) {
                if (this.isEnabled()) {
                    return;
                }

                this.__params = params;
                this.enabled = true;

                if (this.isAjaxMode()) {
                    this.__poll(true);
                }

            },

            /**
             * Returns true if the progress bar is enabled
             *
             * @method
             * @name RichFaces.ui.ProgressBar#isEnabled
             * @return {boolean} true if the progress bar is enabled
             */
            isEnabled: function() {
                return this.enabled;
            },

            destroy: function() {
                this.disable();
                this.__elt = null;
                $super.destroy.call(this);
            }
        }
    }()));

})(RichFaces.jQuery, RichFaces);
