/**
 * @author Lukas Fryc
 */

(function($, richfaces) {

    richfaces.Event = richfaces.Event || {};
    
    var jsfAjaxRequest = jsf.ajax.request;

    jsf.ajax.request = function request(source, event, options) {
        var element, form;
        var source = source;
        
        if (typeof source === 'string') {
            element = document.getElementById(source);
        } else if (typeof source === 'object') {
            element = source;
        } else {
            throw new Error("jsf.request: source must be object or string");
        }
        
        if ($(element).is('form')) {
            form = source;
        } else {
            form = $('form').has(element).get(0);
        }
        
        if (form) {
            $(form).trigger('ajaxsubmit');
        }
        
        // event source re-targeting (javax.faces.source)
        if (richfaces && richfaces.$) {
            if (!richfaces.$(element)) {
                $(element).parents().each(function() {
                    if (richfaces.$(this)) {
                        source = this;
                        return false;
                    }
                });
            }
        }
        
        jsfAjaxRequest(source, event, options);
    };

})(jQuery, window.RichFaces || (window.RichFaces = {}));