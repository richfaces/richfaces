(function($, rf) {

    rf.csv = rf.csv || {};


    var RE_MESSAGE_PATTERN = /\{(\d+)\}/g;

    function _interpolateMessage(customMessage, values) {
        var message = customMessage || "";
        if (message) {
            var msgObject = message.replace(RE_MESSAGE_PATTERN, "\n$1\n").split("\n");
            var value;
            for (var i = 1; i < msgObject.length; i += 2) {
                value = values[msgObject[i]];
                msgObject[i] = typeof value == "undefined" ? "" : value;
            }
            message = msgObject.join('');
        }
        return message;
    }

    $.extend(rf.csv, {
            RE_DIGITS: /^-?\d+$/,
            RE_FLOAT: /^(-?\d+)?(\.(\d+)?(e[+-]?\d+)?)?$/,
            // Messages API
            getMessage :function(facesMessage, values) {
                return {detail:_interpolateMessage(facesMessage.detail, values),summary:_interpolateMessage(facesMessage.summary, values)};
            },
            sendMessage: function (componentId, message) {
                rf.Event.fire(window.document, rf.Event.MESSAGE_EVENT_TYPE, {'sourceId':componentId, 'message':message});
            },
            clearMessage: function (componentId) {
                rf.Event.fire(window.document, rf.Event.MESSAGE_EVENT_TYPE, {'sourceId':componentId, 'message':''});
            },
            getValue: function (clientId, element) {
                var value;
                var element = element || rf.getDomElement(id);
                if (element.value) {
                    value = element.value;
                } else {
                    var component = rf.$(element);
                    // TODO: add getValue to baseComponent and change jsdocs
                    value = component && typeof component["getValue"] == "function" ? component.getValue() : "";
                }
                return value;
            },
            validate: function (event, id, converter, validators) {
                var value = getValue(id);
                if (converter) {
                    try {
                        converter.options.componentId = id;
                        value = getConverter([converter.name])(value, converter.options);
                    } catch (e) {
                        sendMessage(id, e.message);
                        return false;
                    }
                }
                if (validators) {
                    var validatorFunction;
                    try {
                        for (i = 0; i < validators.length; i++) {
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
            // Converters
            convertBoolean: function(value, message, options) {
                options = options || {};
                var result;
                value = $.trim(value).toLowerCase();
                result = value == 'true' ? true : value.length < 1 ? null : false;
                return result;
            },
            // Validators
            validateLength: function(value, message, params) {
                if (params.maximum && value.length > params.maximum) {
                    throw rf.csv.getMessage(message, [params.minimum,params.maximum]);
                }
                if (params.minimum && value.length < params.minimum) {
                    throw rf.csv.getMessage(message, [params.minimum,params.maximum]);
                }

            },
            addFormValidators: function (formId, callValidatorFunctions) {

            }
        });

})(jQuery, window.RichFaces || (window.RichFaces = {}));