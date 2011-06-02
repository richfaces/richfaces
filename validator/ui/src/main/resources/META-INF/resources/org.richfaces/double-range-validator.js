RichFaces.csv.addValidator({"double-range":
        (function(rf) {
            return function (componentId, value, params) {

                var type = typeof value;
                if (type != "number") {
                    if (type != "string") {
                        throw rf.csv.getMessage(params.customMessage, 'DOUBLE_RANGE_VALIDATOR_TYPE', [componentId, ""]);
                    } else {
                        value = $.trim(value);
                        if (!rf.csv.RE_FLOAT.test(value) || (value = parseFloat(value)) == NaN) {
                            throw rf.csv.getMessage(params.customMessage, 'DOUBLE_RANGE_VALIDATOR_TYPE', [componentId, ""]);
                        }
                    }
                }

                var isMinSet = typeof params.minimum == "number";
                var isMaxSet = typeof params.maximum == "number";

                if (isMaxSet && value > params.maximum) {
                    if (isMinSet) {
                        throw rf.csv.getMessage(params.customMessage, 'DOUBLE_RANGE_VALIDATOR_NOT_IN_RANGE', [params.minimum, params.maximum, componentId]);
                    } else {
                        throw rf.csv.getMessage(params.customMessage, 'DOUBLE_RANGE_VALIDATOR_MAXIMUM', [params.maximum, componentId]);
                    }
                }
                if (isMinSet && value < params.minimum) {
                    if (isMaxSet) {
                        throw rf.csv.getMessage(params.customMessage, 'DOUBLE_RANGE_VALIDATOR_NOT_IN_RANGE', [params.minimum, params.maximum, componentId]);
                    } else {
                        throw rf.csv.getMessage(params.customMessage, 'DOUBLE_RANGE_VALIDATOR_MINIMUM', [params.minimum, componentId]);
                    }
                }
            }
        })(window.RichFaces || (window.RichFaces = {}))
    });