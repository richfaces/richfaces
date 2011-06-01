RichFaces.csv.addValidator({"required":
        (function(rf) {
            return function (componentId, value, params) {
                if (value.length == 0) {
                    throw rf.csv.getMessage(params.customMessage, 'UIINPUT_REQUIRED', [componentId]);
                }
            }
        })(window.RichFaces || (window.RichFaces = {}))
    });