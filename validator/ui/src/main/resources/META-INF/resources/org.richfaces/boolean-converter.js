RichFaces.csv.addConverter({"boolean":
        (function($, rf) {
            return function (value, params) {
                var result;
                value = $.trim(value).toLowerCase();
                result = value == 'true' ? true : value.length < 1 ? null : false;

                return result;
            }
        })(jQuery, window.RichFaces || (window.RichFaces = {}))
    });