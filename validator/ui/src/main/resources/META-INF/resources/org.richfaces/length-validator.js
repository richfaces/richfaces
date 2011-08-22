RichFaces.csv.addValidator({"length":
        (function(rf) {
            return function (componentId, value, params) {
                if (params.maximum && value.length > params.maximum) {
                    throw rf.csv.getMessage(params.customMessage, 'LENGTH_VALIDATOR_MAXIMUM', [params.maximum, componentId]);
                }
                if (params.minimum && value.length < params.minimum) {
                    throw rf.csv.getMessage(params.customMessage, 'LENGTH_VALIDATOR_MINIMUM', [params.minimum, componentId]);
                }
            }
        })(window.RichFaces || (window.RichFaces = {}))
    });