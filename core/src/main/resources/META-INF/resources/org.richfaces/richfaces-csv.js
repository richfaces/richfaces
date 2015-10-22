/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

window.RichFaces = window.RichFaces || {};
RichFaces.jQuery = RichFaces.jQuery || window.jQuery;

(function($, rf) {

    rf.csv = rf.csv || {};

    var _messages = {};

    var RE_MESSAGE_PATTERN = /\'?\{(\d+)\}\'?/g;

    var interpolateMessage = function (message, originalValue, values) {
        if (message) {
            var msgObject = message.replace(RE_MESSAGE_PATTERN, "\n$1\n").split("\n");
            var value;
            values[9] = originalValue;
            for (var i = 1; i < msgObject.length; i += 2) {
                value = values[msgObject[i]];
                msgObject[i] = typeof value == "undefined" ? "" : value;
            }
            return msgObject.join('');
        } else {
            return "";
        }
    }

    var _value_query = function(control) {
        if (null !== control.value && undefined != control.value) {
            return control.value;
        } else {
            return "";
        }
    };

    var _check_query = function(control) {
        if (control.checked) {
            return true;
        } else {
            return false;
        }
    };

    var _addOption = function(value, option) {
        if (option.selected) {
            return value[value.length] = option.value;
        }

    };

    var valueExtractors = {
        hidden : function(control) {
            return _value_query(control);
        },

        text : function(control) {
            return _value_query(control);
        },

        textarea : function(control) {
            return _value_query(control);
        },

        'select-one' : function(control) {
            if (control.selectedIndex != -1) {
                return _value_query(control);
            }
        },

        password : function(control) {
            return _value_query(control);
        },

        file : function(control) {
            return _value_query(control);
        },

        radio : function(control) {
            return _check_query(control);
        },

        checkbox : function(control) {
            return _check_query(control);
        },


        'select-multiple' : function(control) {
            var cname = control.name;
            var childs = control.childNodes;
            var value = [];
            for (var i = 0; i < childs.length; i++) {
                var child = childs[i];
                if (child.tagName === 'OPTGROUP') {
                    var options = child.childNodes;
                    for (var j = 0; j < options.length; j++) {
                        value = _addOption(value, options[j]);
                    }
                } else {
                    value = _addOption(value, child);
                }
            }
            return value;
        },

        // command inputs


        // same as link, but have additional field - control, for input
        // submit.
        input : function(control) {
            return _value_query(control);
        }
    };

    var getValue = function(element) {
        var value = "";
        if (valueExtractors[element.type]) {
            value = valueExtractors[element.type](element);
        } else if (undefined !== element.value) {
            value = element.value;
        } else {
            var component = $(element);
            // TODO: add getValue to baseComponent and change jsdocs
            if (component) {
                if (typeof rf.component(component)["getValue"] === "function") {
                    value = rf.component(component).getValue();
                } else {
                    var nestedComponents = $("*", component).filter(":editable");
                    if (nestedComponents) {
                        var nestedComponent = nestedComponents[0];
                        value = valueExtractors[nestedComponent.type](nestedComponent);
                    }
                }
            }
        }
        return value;
    }

    var getLabel = function(component, id) {
        if (component.p) {
            return component.p.label || id;
        }
        return id;
    }

    $.extend(rf.csv, {
            RE_DIGITS: /^-?\d+$/,
            RE_FLOAT: /^(-?\d+)?(\.(\d+)?(e[+-]?\d+)?)?$/,
            // Messages API
            addMessage: function (messagesObject) {
                $.extend(_messages, messagesObject);
            },
            getMessage: function(customMessage, originalValue, values, messageId) {
                var message = customMessage ? customMessage : _messages[messageId] || {detail:"",summary:"",severity:0};
                return {detail:interpolateMessage(message.detail, originalValue, values),summary:interpolateMessage(message.summary, originalValue, values),severity:message.severity};
            },
            sendMessage: function (componentId, message) {
                rf.Event.fire(window.document, rf.Event.MESSAGE_EVENT_TYPE, {'sourceId':componentId, 'message':message});
            },
            clearMessage: function(componentId) {
                rf.Event.fire(window.document, rf.Event.MESSAGE_EVENT_TYPE, {'sourceId':componentId });
            },
            validate: function (event, element, params) {
                var element = rf.getDomElement(element);
                var value = getValue(element);
                var convertedValue;
                var converter = params.c;
                var id = element.id;
                rf.csv.clearMessage(id);
                if (converter) {
                    var label = getLabel(converter, id);
                    try {
                        if (converter.f)
                            convertedValue = converter.f(value, id, getLabel(converter, id), converter.m);
                    } catch (e) {
                        e.severity = 2;
                        rf.csv.sendMessage(id, e);
                        return false;
                    }
                } else {
                    convertedValue = value;
                }
                var result = true
                var validators = params.v;
                var validationErrorMessage;
                if (validators) {
                    var validatorFunction,validator;
                    for (var i = 0; i < validators.length; i++) {
                        try {
                            validator = validators[i];
                            validatorFunction = validator.f;
                            if (validatorFunction) {
                                validatorFunction(convertedValue, getLabel(validator, id), validator.p, validator.m);
                            }
                        } catch (e) {
                            validationErrorMessage = e;
                            e.severity = 2;
                            rf.csv.sendMessage(id, e);
                            result = false;
                        }
                    }
                }
                if (!result && params.oninvalid instanceof Function) {
                    params.oninvalid([validationErrorMessage]);
                }
                if (result) {
                    if (!params.da && params.a) {
                        params.a.call(element, event, id);
                    } else if (params.onvalid instanceof Function) {
                        params.onvalid();
                    }
                }
                return result;
            }
        });

    /*
     * convert all natural number formats
     *
     */
    var _convertNatural = function(value, label, msg, min, max, sample) {
        var result = null,
            originalValue = value;
        if (value) {
            value = $.trim(value);
            if (!rf.csv.RE_DIGITS.test(value) || (result = parseInt(value, 10)) < min || result > max) {
                throw rf.csv.getMessage(msg, originalValue, sample ? [value, sample, label] : [value,label]);
            }
        }
        return result;
    }

    var _convertReal = function(value, label, msg, sample) {
        var result = null,
            originalValue = value;
        if (value) {
            value = $.trim(value);
            if (!rf.csv.RE_FLOAT.test(value) || isNaN(result = parseFloat(value))) {
                // TODO - check Float limits.
                throw rf.csv.getMessage(msg, originalValue, sample ? [value, sample, label] : [value,label]);
            }
        }
        return result;
    }
    /*
     * Converters implementation
     */
    $.extend(rf.csv, {
            "convertBoolean": function (value, label, params, msg) {
                if (typeof value === "string") {
                    var lcvalue = $.trim(value).toLowerCase();
                    if (lcvalue === 'on' || lcvalue === 'true' || lcvalue === 'yes') {
                        return true;
                    }
                } else if (true === value) {
                    return true;
                }
                return false;
            },
            "convertDate": function (value, label, params, msg) {
                var result;
                value = $.trim(value);
                // TODO - JSF date converter options.
                result = Date.parse(value);
                return result;
            },
            "convertByte": function (value, label, params, msg) {
                return _convertNatural(value, label, msg, -128, 127, 254);
            },
            "convertNumber": function (value, label, params, msg) {
                var result,
                    originalValue = value;
                value = $.trim(value);
                result = parseFloat(value);
                if (isNaN(result)) {
                    throw rf.csv.getMessage(msg, originalValue, [value, 99, label]);
                }
                return result;
            },
            "convertFloat": function (value, label, params, msg) {
                return _convertReal(value, label, msg, 2000000000);
            },
            "convertDouble": function (value, label, params, msg) {
                return _convertReal(value, label, msg, 1999999);
            },
            "convertShort": function (value, label, params, msg) {
                return _convertNatural(value, label, msg, -32768, 32767, 32456);
            },
            "convertInteger": function (value, label, params, msg) {
                return _convertNatural(value, label, msg, -2147483648, 2147483648, 9346);
            },
            "convertCharacter": function (value, label, params, msg) {
                return _convertNatural(value, label, msg, 0, 65535);
            },
            "convertLong": function (value, label, params, msg) {
                return _convertNatural(value, label, msg, -9223372036854775808, 9223372036854775807, 98765432);
            }
        });

    var validateRange = function(originalValue, value, label, params, msg) {
        var isMinSet = typeof params.min === "number";// && params.min >0;
        var isMaxSet = typeof params.max === "number";// && params.max >0;

        if (isMaxSet && value > params.max) {
            throw rf.csv.getMessage(msg, originalValue, isMinSet ? [params.min,params.max,label] : [params.max,label]);
        }
        if (isMinSet && value < params.min) {
            throw rf.csv.getMessage(msg, originalValue, isMaxSet ? [params.min,params.max,label] : [params.min,label]);
        }
    };

    var validateRegex = function(value, label, pattern, msg) {
        if (typeof pattern != "string" || pattern.length == 0) {
            throw rf.csv.getMessage(msg, value, [], 'REGEX_VALIDATOR_PATTERN_NOT_SET');
        }

        var matchPattern = makePatternAMatch(pattern);
        var re;
        try {
            re = new RegExp(matchPattern);
        } catch (e) {
            throw rf.csv.getMessage(msg, value, [], 'REGEX_VALIDATOR_MATCH_EXCEPTION');
        }
        if (!re.test(value)) {
            throw rf.csv.getMessage(msg, value, [pattern,label]);
        }

    };

    var makePatternAMatch = function(pattern) {
        if (! (pattern.slice(0, 1) === '^') ) {
            pattern = '^' + pattern;
        }
        if (! (pattern.slice(-1) === '$') ) {
            pattern = pattern + '$';
        }
        return pattern;
    }
    /*
     * Validators implementation
     */
    $.extend(rf.csv, {
            "validateLongRange": function (value, label, params, msg) {
                var type = typeof value,
                    originalValue = value;
                if (type !== "number") {
                    if (type != "string") {
                        throw rf.csv.getMessage(msg, value, [componentId, ""], 'LONG_RANGE_VALIDATOR_TYPE');
                    } else {
                        value = $.trim(value);
                        if (!rf.csv.RE_DIGITS.test(value) || (value = parseInt(value, 10)) == NaN) {
                            throw rf.csv.getMessage(msg, value, [componentId, ""], 'LONG_RANGE_VALIDATOR_TYPE');
                        }
                    }
                }

                validateRange(originalValue, value, label, params, msg);
            },
            "validateDoubleRange": function (value, label, params, msg) {
                var type = typeof value,
                    originalValue = value;
                if (type !== "number") {
                    if (type !== "string") {
                        throw rf.csv.getMessage(msg, value, [componentId, ""], 'DOUBLE_RANGE_VALIDATOR_TYPE');
                    } else {
                        value = $.trim(value);
                        if (!rf.csv.RE_FLOAT.test(value) || (value = parseFloat(value)) == NaN) {
                            throw rf.csv.getMessage(msg, value, [componentId, ""], 'DOUBLE_RANGE_VALIDATOR_TYPE');
                        }
                    }
                }

                validateRange(originalValue, value, label, params, msg);
            },
            "validateLength": function (value, label, params, msg) {
                var length = value ? value.length : 0;
                validateRange(value, length, label, params, msg);
            },
            "validateSize": function (value, label, params, msg) {
                var length = value ? value.length : 0;
                validateRange(value, length, label, params, msg);
            },
            "validateRegex": function (value, label, params, msg) {
                validateRegex(value, label, params.pattern, msg);
            },
            "validatePattern": function (value, label, params, msg) {
                validateRegex(value, label, params.regexp, msg);
            },
            "validateRequired": function (value, label, params, msg) {
                if (undefined === value || null === value || "" === value) {
                    throw rf.csv.getMessage(msg, value, [label]);
                }
            },
            "validateTrue": function (value, label, params, msg) {
                if (value !== true) {
                    throw msg;
                }
            },
            "validateFalse": function (value, label, params, msg) {
                if (value !== false) {
                    throw msg;
                }
            },
            "validateMax": function (value, label, params, msg) {
                if (value > params.value) {
                    throw msg;
                }
            },
            "validateMin": function (value, label, params, msg) {
                if (value < params.value) {
                    throw msg;
                }
            }
        });

})(RichFaces.jQuery, RichFaces);