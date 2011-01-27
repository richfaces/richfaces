(function($, rf) {

	rf.csv = rf.csv || {};
	
	var _messages = {};
	var _validators = {};
	var _converters = {};

	var RE_MESSAGE_PATTERN = /\'?\{(\d+)\}\'?/g;
	
	var __interpolateMessage = function (message, values) {
		if (message) {
			var msgObject = message.replace(RE_MESSAGE_PATTERN,"\n$1\n").split("\n");
			var value;
			for (var i=1; i<msgObject.length; i+=2) {
				value = values[msgObject[i]];
				msgObject[i] = typeof value == "undefined" ? "" : value;
			}
			return msgObject.join('');
		} else {
		    return "";
		}
	}
	
	var _interpolateFacesMessage = function(msg,id,value){
		var d = msg.detail?_substitute(msg.detail,id,value):"";
		var s = msg.summary?_substitute(msg.summary,id,value):"";
		return {detail:d,summary:s};
	}
	
	var _substitute = function(msg,id,value){
		return msg.replace("{id}",id).replace("{value}",value);
	}

	var __getValue = function(id) {
		var value;
		var element = rf.getDomElement(id);
		if (element.value) {
			value = element.value;
		} else {
			var component = rf.$(element);
			// TODO: add getValue to baseComponent and change jsdocs
			value = component && typeof component["getValue"] == "function" ? component.getValue() : "";
		}
		return value;
	}
	
	$.extend(rf.csv, {
		RE_DIGITS: /^-?\d+$/,
		RE_FLOAT: /^(-?\d+)?(\.(\d+)?(e[+-]?\d+)?)?$/,
		// Messages API
		addMessage: function (messagesObject) {
			$.extend(_messages, messagesObject);
		},
		getMessage: function(customMessage, messageId, values) {
			var message = customMessage ? customMessage : _messages[messageId] || {detail:"",summary:"",severity:0};
			return {detail:__interpolateMessage(message.detail,values),summary:__interpolateMessage(message.summary,values),severity:message.severity};
		},
		interpolateMessage: function(message,values){
			return {detail:__interpolateMessage(message.detail,values),summary:__interpolateMessage(message.summary,values),severity:message.severity};			
		},
		sendMessage: function (componentId, message) {
			rf.Event.fire(window.document, rf.Event.MESSAGE_EVENT_TYPE, {'sourceId':componentId, 'message':message});
		},
		clearMessage: function(componentId){
			rf.Event.fire(window.document, rf.Event.MESSAGE_EVENT_TYPE, {'sourceId':componentId });
		},
		validate: function (event, id, element, params) {
			var value = __getValue(element || id);
			var convertedValue;
			var converter = params.c;
			if (converter) {
				try {
					if (converter.f)
						convertedValue = converter.f(value,id,converter.p,converter.m);
				} catch (e){
					e.severity=2;
					rf.csv.sendMessage(id, e);
					return false;
				}
			} else {
				convertedValue = value;
			}
			var validators = params.v;
			if (validators) {
				var validatorFunction;
				try {
					for (i=0;i<validators.length;i++) {
						validatorFunction = validators[i].f;
						if (validatorFunction) {
							validatorFunction(convertedValue,id, validators[i].p,validators[i].m);
						}
					}
				} catch (e) {
					e.severity=2;
					rf.csv.sendMessage(id, e);
					return false;
				}
			}
			if(!params.da && params.a){
				params.a.call(element,event,id);
			} else {
				rf.csv.clearMessage(id);
			}
			return true;
		},
		/*
			function form_3Atext_3Av(clientId,element,event,disableAjax){var value=RichFaces.csv.getValue(clientId,element);
			try {
			var convertedValue=value;
			RichFaces.csv.validateLength(convertedValue,{"detail":"{2}: Validation Error: Specified attribute is not between the expected values of {0} and {1}.","severity":0,"summary":"{2}: Validation Error: Specified attribute is not between the expected values of {0} and {1}."} ,{"minimum":1,"maximum":3} );
			if(!disableAjax){
			RichFaces.csv.clearMessage(clientId);
			}
			return true;
			} catch(e) {
			RichFaces.csv.sendMessage(clientId,e);
			return false;
			}}
		 */
		/*
		 *  c: {name:, params:}
		 	v: [{f:, customMessage:, [validator params]}]
		 	function form_3Atext_3Av(id, el, e, c, v, p) {
		 		RichFaces.csv.validate(e, id, el, c, v, p);
		 	}
		 */
	});
	
	/*
	 * Converters implementation
	 */	
	$.extend(rf.csv, {	
		"convertBoolean": function (value,label,params,msg) {
			var result; value = $.trim(value).toLowerCase();
			result = value=='true' ? true : value.length<1 ? null : false;
			
			return result;
		},
		"convertByte": function (value,label,params,msg) {
			var result; value = $.trim(value);
			if (!rf.csv.RE_DIGITS.test(value) || (result=parseInt(value,10))<-128 || result>127) {
				throw rf.csv.interpolateMessage(msg,  [value, 254, label]);
			}
			return result;
		},
		"convertNumber": function (value,label,params,msg) {
			var result; value=$.trim(value);
			result = parseFloat(value);
			if (isNaN(result)) {
					throw rf.csv.interpolateMessage(msg,  [value, 99, label]);
			}
			return result;
		},
		"convertShort": function (value,label,params,msg) {
			var result; value = $.trim(value);
			if (!rf.csv.RE_DIGITS.test(value) || (result=parseInt(value,10))<-32768 || result>32767) {
				throw rf.csv.interpolateMessage(msg,  [value, 32456, label]);
			}
			return result;
		}
	});
	
	var validateRange = function(value,label,params,msg) {
		var isMinSet = typeof params.minimum == "number" ;//&& params.minimum >0;
		var isMaxSet = typeof params.maximum == "number" ;//&& params.maximum >0;

		if (isMaxSet && value > params.maximum) {
			throw rf.csv.interpolateMessage(msg,isMinSet?[params.minimum,params.maximum,label]:[params.maximum,label]);
		}
		if (isMinSet && value < params.minimum) {
			throw rf.csv.interpolateMessage(msg,isMaxSet?[params.minimum,params.maximum,label]:[params.minimum,label]);
		}
	};

	var validateRegex = function(value,label,pattern,msg) {
		if (typeof pattern != "string" || pattern.length == 0) {
			throw rf.csv.getMessage(msg, 'REGEX_VALIDATOR_PATTERN_NOT_SET', []);
		}
		
		var re;
		try {
			re = new RegExp(pattern);
		} catch (e) {
			throw rf.csv.getMessage(msg, 'REGEX_VALIDATOR_MATCH_EXCEPTION', []);
		}
		if (!re.test(value)){
			throw rf.csv.interpolateMessage(msg, [pattern,label]);
		}

	}
	/*
	 * Validators implementation
	 */
	$.extend(rf.csv, {	
		"validateLongRange": function (value,label,params,msg) {
			var type = typeof value;
			if (type != "number") {
				if (type != "string") {
					throw rf.csv.getMessage(msg, 'LONG_RANGE_VALIDATOR_TYPE', [componentId, ""]);
				} else {
					value = $.trim(value);
					if (!rf.csv.RE_DIGITS.test(value) || (value = parseInt(value, 10))==NaN) {
						throw rf.csv.getMessage(msg, 'LONG_RANGE_VALIDATOR_TYPE', [componentId, ""]);
					}
				}
			}
			
			validateRange(value,label,params,msg);
		},
		"validateDoubleRange": function (value,label,params,msg) {
			var type = typeof value;
			if (type != "number") {
				if (type != "string") {
					throw rf.csv.getMessage(msg, 'DOUBLE_RANGE_VALIDATOR_TYPE', [componentId, ""]);
				} else {
					value = $.trim(value);
					if (!rf.csv.RE_FLOAT.test(value) || (value = parseFloat(value))==NaN) {
						throw rf.csv.getMessage(msg, 'DOUBLE_RANGE_VALIDATOR_TYPE', [componentId, ""]);
					}
				}
			}
			
			validateRange(value,label,params,msg);
		},
		"validateLength": function (value,label,params,msg) {
			var length = value?value.length:0;
			validateRange(length,label,params,msg);
		},
		"validateSize": function (value,label,params,msg) {
			var length = value?value.length:0;
			validateRange(length,label,{maximum:params.max,minimum:params.min},msg);
		},
		"validateRegex": function (value,label,params,msg) {
			validateRegex(value,label,params.pattern,msg);
		},
		"validatePattern": function (value,label,params,msg) {
			validateRegex(value,label,params.regexp,msg);
		},
		"validateRequired": function (value,label,params,msg) {
	        if (!value ) {
	        	throw rf.csv.interpolateMessage(msg, [label]);
	        }
		},
		"validateMax": function (value,label,params,msg) {
	        if (value > params.value ) {
	        	throw msg;
	        }
		},
		"validateMin": function (value,label,params,msg) {
	        if (value < params.value ) {
	        	throw msg;
	        }
		}
	});
	
})(jQuery, window.RichFaces || (window.RichFaces={}));