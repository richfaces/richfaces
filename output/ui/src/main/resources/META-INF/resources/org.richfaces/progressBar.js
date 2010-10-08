//ProgressBar = {};
//ProgressBar = Class.create();
(function ($, rf) {

	rf.ui = rf.ui || {}; 
	
	// Constructor definition
	rf.ui.ProgressBar = function(componentId, options) {
		// call constructor of parent class
		$super.constructor.call(this, componentId);
		this.id = componentId;
		this.attachToDom(this.id);
		this.options = $.extend({}, defaultOptions, options);
		var f = this.getForm();
		this.formId = (f) ? f.id : null;
		this.disabled = false;
		this.state = this.options.state;
		this.value = this.options.value;
		this.minValue = this.options.minValue;
		this.maxValue = this.options.maxValue;
		var component = this;
		this.options.beforedomupdate = function(event) {
            component.onComplete(event.data);
        }
		
	};
	
	// Extend component class and add protected methods from parent class to our container
	rf.BaseComponent.extend(rf.ui.ProgressBar);
	
	// define super class link
	var $super = rf.ui.ProgressBar.$super;
	
	var defaultOptions = {
		mode: "ajax",
		minValue: 0,
		maxValue: 100,
		state : "initialState"
	};
	
	var getForm = function () {
		var parentForm = rf.getDomElement(this.id);
		while (parentForm.tagName && parentForm.tagName.toLowerCase() != 'form') {
			parentForm = parentForm.parentNode;
		}
		return parentForm;
	};
	
	var getValue = function () {
		return this.value;
	};
	
	var getParameter = function (ev, params, paramName) {
		if (!params) {
			params = ev;
		}
		if (params && params[paramName]) {
			return params[paramName];
		} 
		return params;
	};
	
	var onComplete = function (data) {
	  	if (!rf.getDomElement(this.id) || this.disabled) { return; } 
		if (data) {
			this.value = data['value'];
			if (this.state == "progressState") {
				if (this.value > this.getMaxValue()) {
					this.options.enabled=false;
					this.forceState("completeState",null);
					return;
				}
				this.updateComponent(data);
				this.renderLabel(data['markup'], data['context']);
			} else if (this.state == "initialState" && this.value > this.getMinValue()) {
				this.state = "progressState";
				this.forceState("progressState");
				return;
			}
			this.poll(); 
		}
		
	};
	var poll = function () {
		if(this.options.enabled){
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
	
	var updateComponent = function (data) {
		this.setValue(this.value);
		if (!data['enabled']) { this.disable(); }
		this.updateClassName(rf.getDomElement(this.id + ":complete"), data['completeClass']);
		this.updateClassName(rf.getDomElement(this.id + ":remain"), data['remainClass']);
		this.updateClassName(rf.getDomElement(this.id), data['styleClass']);
		
		if (this.options.pollinterval != data['interval']) {
			this.options.pollinterval = data['interval'];
		}
	};
	
	var updateClassName = function (o, newName) {
	if (!newName) return;
		if (o && o.className) {
			if (o.className.indexOf(newName) < 0){
				o.className = o.className + " " + newName;
			}
		}
	};
	var getContext = function () {
		var context = this.context;
		if (!context) { context = {}; }
			context['minValue'] = (this.minValue == 0 ? "0" : this.minValue);
			context['maxValue'] = (this.maxValue == 0 ? "0" : this.maxValue);
			context['value'] = (this.value == 0 ? "0" : this.value);
			if (this.progressVar) {
				context[this.progressVar] = context['value'];
			}
		return context;
	};

	var getMode = function () {
		return this.mode;
	};
	var getMaxValue = function () {
		return this.maxValue;	
	};
	var getMinValue = function () {
		return this.minValue;
	};
	var isAjaxMode = function () {
		return (this.getMode() == "ajax");
	}; 
	var calculatePercent = function(v) {
		var min = parseFloat(this.getMinValue());
		var max = parseFloat(this.getMaxValue());
		var value = parseFloat(v);
		if (value > min && value < max) {
			return (100*(value - min))/(max - min); 
		} else if (value <= min) {
			return 0;
		} else if (value >= max) {
			return 100;
		}
	};
	var setValue = function (ev, val) {
		val = this.getParameter(ev, val, "value");
		this.value = val;
		if (!this.isAjaxMode()) {
			if (parseFloat(val) <=  parseFloat(this.getMinValue())) {
				this.switchState("initialState");
			}else if (parseFloat(val) > parseFloat(this.getMaxValue())) {
				this.switchState("completeState");
			}else {
				this.switchState("progressState");
			}
		}
		if (!this.isAjaxMode() && this.state != "progressState") return;
		
		if (this.markup) {
			this.renderLabel(this.markup, this.getContext());
		}
		var p = this.calculatePercent(val);
		var d = $(rf.getDomElement(this.id + ":upload"));
		if (d != null) d.css('width', p + "%");
	
	};
	
	var renderLabel = function (markup, context) {
		if (!markup || this.state != "progressState") {
			return;
		}
		if (!context) {
			context = this.getContext();
		}
		var html = this.interpolate(markup, context);
		var remain = rf.getDomElement(this.id + ":remain");
		var complete = rf.getDomElement(this.id + ":complete");
		if(remain && complete){
			remain.innerHTML = complete.innerHTML = html;
		}
		
	};
	var interpolate = function (placeholders, context) {
		for(var k in context) {
			var v = context[k];
			var regexp = new RegExp("\\{" + k + "\\}", "g");
			placeholders = placeholders.replace(regexp, v);
		}
		return placeholders;
	};

	var enable = function (ev) {
		if (!this.isAjaxMode()) {
			this.switchState("progressState");
			this.setValue(this.getMinValue() + 1);
		}else if (!(this.value > this.getMaxValue())) {
			this.disable();
			this.poll();			
		}
	this.disabled = false;
	};
	var disable = function () {
		this.disabled = true;
	};
	var finish = function () {
		if (!this.isAjaxMode()) {
			this.switchState("completeState");
		}else {
			this.disable();
			this.forceState("complete");
		}
	};
	var hideAll = function () {
		$(rf.getDomElement(this.id + ":progressState")).hide();
		$(rf.getDomElement(this.id + ":completeState")).hide();
		$(rf.getDomElement(this.id + ":initialState")).hide();
	};
	var switchState = function (state) {
		this.state = state;
		this.hideAll();
		$(rf.getDomElement(this.id + ":" + state)).show();
	};
	var renderState = function (state) {
		this.state = state;
		this.hideAll();
		$(rf.getDomElement(this.id + ":" + state)).show();
	};
	var forceState = function (state, oncomplete) {
		var options = {};
		options['parameters'] = options['parameters'] || {};
		options['parameters']['forcePercent'] = state;
		options['parameters'][this.id] = this.id;
		if (oncomplete) {
			options['oncomplete'] = oncomplete;
		}
		rf.ajax(this.formId, null, options);
	};
	
	/*
	 * Prototype definition
	 */
	$.extend(rf.ui.ProgressBar.prototype, (function () {
		return {
			/*
			 * public API functions
			 */
 			name:"ProgressBar",
 			getForm: getForm,
 			getValue: getValue,
 			getParameter: getParameter,
 			onComplete: onComplete,
 			poll: poll,
 			updateComponent: updateComponent,
 			updateClassName: updateClassName,
 			getContext: getContext,
 			getMode: getMode,
 			getMaxValue: getMaxValue,
 			getMinValue: getMinValue,
 			isAjaxMode: isAjaxMode,
 			calculatePercent: calculatePercent,
 			setValue: setValue,
 			enable: enable,
 			disable: disable,
 			finish: finish,
 			hideAll: hideAll,
 			interpolate: interpolate,
 			renderLabel: renderLabel,
 			switchState: switchState,
 			renderState: renderState,
 			forceState: forceState
 			
		};
	})());
})(jQuery, RichFaces);
