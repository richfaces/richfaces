//ProgressBar = {};
//ProgressBar = Class.create();
(function ($, rf) {

	rf.ui = rf.ui || {}; 
	
	var defaultOptions = {
		mode: "ajax",
		minValue: 0,
		maxValue: 100,
		state: "initialState"
	};

	// Constructor definition
	rf.ui.ProgressBar = function(componentId, options) {
		// call constructor of parent class
		$super.constructor.call(this, componentId);
		this.id = componentId;
		this.attachToDom(this.id);
		this.options = $.extend({}, defaultOptions, options);
		this.enabled = this.options.enabled;
		this.state = this.options.state;
		this.value = this.options.value;
		this.minValue = this.options.minValue;
		this.maxValue = this.options.maxValue;
		var component = this;
		this.options.beforedomupdate = function(event) {
            component.__onComplete(event.data);
        }
		
	};
	
	// Extend component class and add protected methods from parent class to our container
	rf.BaseComponent.extend(rf.ui.ProgressBar);
	
	// define super class link
	var $super = rf.ui.ProgressBar.$super;
	
	$.extend(rf.ui.ProgressBar.prototype, (function() {
		return {
 			name:"ProgressBar",
 			
 			__updateComponent: function (data) {
 				this.setValue(this.value);
 				if (!data['enabled']) { 
 					this.disable(); 
 				}
 				$(rf.getDomElement(this.id + ":complete")).addClass(data['completeClass']);
 				$(rf.getDomElement(this.id + ":remain")).addClass(data['remainClass']);
 				$(rf.getDomElement(this.id)).addClass(data['styleClass']);
 				
 				if (this.options.pollinterval != data['interval']) {
 					this.options.pollinterval = data['interval'];
 				}
 			},
 			
 			__forceState: function (state, oncomplete) {
 				var params = {};
 				params['forcePercent'] = state;
 				params[this.id] = this.id;
 				
 				var options = {
 					parameters: params,	
 					oncomplete: oncomplete
 				};
 				
 				var form = $(rf.getDomElement(this.id)).closest('form');
 				rf.ajax(form.attr('id'), null, options);
 			},
 			
 			__getContext: function () {
 				var context = this.context || {};
				context['minValue'] = (this.minValue == 0 ? "0" : this.minValue);
				context['maxValue'] = (this.maxValue == 0 ? "0" : this.maxValue);
				context['value'] = (this.value == 0 ? "0" : this.value);
				if (this.progressVar) {
					context[this.progressVar] = context['value'];
				}
 				return context;
 			},

 			__renderLabel: function (markup, context) {
 				if (!markup || this.state != "progressState") {
 					return;
 				}
 				var html = rf.interpolate(markup, context || this.__getContext());
 				var remain = rf.getDomElement(this.id + ":remain");
 				var complete = rf.getDomElement(this.id + ":complete");
 				if (remain && complete){
 					remain.innerHTML = complete.innerHTML = html;
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
 			
			getValue: function() {
				return this.value;
			},
			
			showState: function (state) {
				this.state = state;
				
				var stateElt = $(rf.getDomElement(this.id + ":" + state));
				stateElt.show().siblings().hide();
			},
			
			setValue: function(val) {
				this.value = this.__getPropertyOrObject(val, "value");
				
				if (!this.isAjaxMode()) {
					if (parseFloat(this.getValue()) <= parseFloat(this.getMinValue())) {
						this.showState("initialState");
					} else if (parseFloat(this.getValue()) > parseFloat(this.getMaxValue())) {
						this.showState("completeState");
					} else {
						this.showState("progressState");
					}
				}
				
				if (this.isAjaxMode() || this.state == "progressState") {
					if (this.markup) {
						this.__renderLabel(this.markup, this.__getContext());
					}
					
					var p = this.__calculatePercent(this.getValue());
					$(rf.getDomElement(this.id + ":upload")).css('width', p + "%");
				}
			},
			
			getMaxValue: function() {
				return this.maxValue;	
			},

			getMinValue: function() {
				return this.minValue;
			},

			getMode: function () {
				return this.options.mode;
			},
			
			isAjaxMode: function () {
				return (this.getMode() == "ajax");
			},
			
			disable: function () {
				this.enabled = false;
			},
			
			enable: function () {
				if (this.isEnabled()) {
					return;
				}
				
				this.enabled = true;

				if (!this.isAjaxMode()) {
					this.showState("progressState");
					this.setValue(this.getMinValue() + 1);
				} else if (this.value <= this.getMaxValue()) {
					this.__poll();			
				}
				
			},
			
			isEnabled: function() {
				return this.enabled;
			}
		}
	} ()));
	
	var onComplete = function (data) {
	  	if (!rf.getDomElement(this.id) || !this.isEnabled()) { 
	  		return; 
	  	} 
		if (data) {
			this.value = data['value'];
			if (this.state == "progressState") {
				if (this.value > this.getMaxValue()) {
					this.disable();
					this.__forceState("completeState");
					return;
				}
				this.__updateComponent(data);
				this.__renderLabel(data['markup'], data['context']);
			} else if (this.state == "initialState" && this.value > this.getMinValue()) {
				this.state = "progressState";
				this.__forceState("progressState");
				return;
			}
			this.__poll(); 
		}
		
	};
	var poll = function () {
		if (this.isEnabled()){
			this.options.parameters = this.options.parameters || {};
			this.options.parameters['percent'] = "percent";
			this.options.parameters[this.id] = this.id;
			var component = this;
			window.setTimeout(function(){
			if(component.options.onsubmit){
				var onsubmit = eval(component.options.onsubmit)
				var result = onsubmit.call(component);
				if (result!=false) {
					 result = true;
				}	 
				if(result){
					rf.ajax(component.options.pollId, null, component.options);
				}
			}else{
				rf.ajax(component.options.pollId, null, component.options);
			}
			
		},this.options.pollinterval);
		}
	};
	
	/*
	 * Prototype definition
	 */
	$.extend(rf.ui.ProgressBar.prototype, (function () {
		return {
 			__onComplete: onComplete,
 			__poll: poll
 		};
	})());
})(jQuery, RichFaces);
