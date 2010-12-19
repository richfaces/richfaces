(function($, rf) {

	rf.csv = rf.csv || {};
	
	var _messages = {};
	var _validators = {};
	var _converters = {};

	var RE_MESSAGE_PATTERN = /\{(\d+)\}/g;
	
	$.extend(rf.csv, {
		RE_DIGITS: /^-?\d+$/,
		RE_FLOAT: /^(-?\d+)?(\.(\d+)?(e[+-]?\d+)?)?$/,
		// Messages API
		addMessage: function (messagesObject) {
			$.extend(_messages, messagesObject);
		},
		getMessage: function(customMessage, messageId, values) {
			var message = customMessage || _messages[messageId] || "";
			if (message) {
				var msgObject = message.replace(RE_MESSAGE_PATTERN,"\n$1\n").split("\n");
				var value;
				for (var i=1; i<msgObject.length; i+=2) {
					value = values[msgObject[i]];
					msgObject[i] = typeof value == "undefined" ? "" : value;
				}
				message = msgObject.join('');
			}
			return {message:message};
		},
		sendMessage: function (componentId, message) {
			rf.Event.fire(window.document, rf.Event.MESSAGE_EVENT_TYPE, {'sourceId':componentId, 'message':message});
		},
		// Converters API
		addConverter: function (converterFunctions) {
			$.extend(_converters, converterFunctions);
		},
		getConverter: function (name) {
			return _converters[name];
		},
		// Validators API
		addValidator: function (validatorFunctions) {
			$.extend(_validators, validatorFunctions);
		},
		getValidator: function (name) {
			return _validators[name];
		},
		validate: function (event, id, converter, validators) {
			var value;
			var element = rf.getDomElement(id);
			if (element.value) {
				value = element.value;
			} else {
				var component = rf.$(element);
				// TODO: add getValue to baseComponent and change jsdocs
				value = component && typeof component["getValue"] == "function" ? component.getValue() : "";
			}
			if (converter) {
				try {
					converter.options.componentId = id;
					value = getConverter([converter.name])(value, converter.options);
				} catch (e){
					sendMessage(id, e.message);
					return false;
				}
			}
			if (validators) {
				var validatorFunction;
				try {
					for (i=0;i<validators.length;i++) {
						validatorFunction = getValidator(validators[i].type);
						if (validatorFunction) {
							validatorFunction(id, value, validators[i]);
						}
					}
				} catch (e) {
					sendMessage(id, result);
					return false;
				}
			}
			return true;
		},
		addFormValidators: function (formId, callValidatorFunctions) {
			
		}
	});
	
})(jQuery, window.RichFaces || (window.RichFaces={}));