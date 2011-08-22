RichFaces.csv.addConverter({"short":
        (function($, rf) {
            return function (value, params) {
                var result;
                value = $.trim(value);
                if (!rf.csv.RE_DIGITS.test(value) || (result = parseInt(value, 10)) < -32768 || result > 32767) {
                    throw rf.csv.getMessage(params.customMessage, 'SHORT_CONVERTER_SHORT', [value, 0, params.componentId]);
                }
                return result;
            }
        })(jQuery, window.RichFaces || (window.RichFaces = {}))
    });