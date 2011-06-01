window.jsf = {};
jsf.ajax = (function() {
    return {
        eventHandlers:[],
        errorHandlers:[],
        eventHandlerCounter:0,
        errorHandlerCounter:0,
        request:function (source, event, options) {
            for (var i = 0; i < jsf.ajax.eventHandlers.length; i++) {
                jsf.ajax.eventHandlers[i]({type:"event", status:"success"});
            }
        },
        addOnEvent: function (handler) {
            jsf.ajax.eventHandlers.push(handler);
            this.eventHandlerCounter++;
        },
        addOnError: function (handler) {
            jsf.ajax.errorHandlers.push(handler);
            this.errorHandlerCounter++;
        }
    }
})();

window.jsf_ajax_request = jsf.ajax.request;