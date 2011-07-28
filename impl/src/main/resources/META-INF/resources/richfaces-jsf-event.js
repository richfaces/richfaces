/**
 * @author Lukas Fryc
 */

(function($, richfaces) {

    richfaces.Event = richfaces.Event || {};
    
    var jsfAjaxRequest = jsf.ajax.request;

    jsf.ajax.request = function request(source, event, options) {
        var element, form;
        
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
        
        if (richfaces.Event && richfaces.Event.fire) {
            richfaces.Event.callHandler(form, 'ajaxsubmit');
        }
        
        jsfAjaxRequest(source, event, options);
    };

})(jQuery, window.RichFaces || (window.RichFaces = {}));