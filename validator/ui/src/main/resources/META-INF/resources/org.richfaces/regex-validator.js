RichFaces.csv.addValidator({"regex":
        (function(rf) {
            return function (componentId, value, params) {

                if (typeof params.pattern != "string" || params.pattern.length == 0) {
                    throw rf.csv.getMessage(params.customMessage, 'REGEX_VALIDATOR_PATTERN_NOT_SET', []);
                }

                var re;
                try {
                    re = new RegExp(params.pattern);
                } catch (e) {
                    throw rf.csv.getMessage(params.customMessage, 'REGEX_VALIDATOR_MATCH_EXCEPTION', []);
                }
                if (!re.test(value)) {
                    throw rf.csv.getMessage(params.customMessage, 'REGEX_VALIDATOR_NOT_MATCHED', [params.pattern]);
                }
            }
        })(window.RichFaces || (window.RichFaces = {}))
    });