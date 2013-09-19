RichFaces.QueueTest = {
    ajax: function(data, options) {
        with (RichFaces) {
            log.info("QueueTest.ajax is called with data: '" + data + "' and options: '" + "'");

            //		var defaultRequestTime = XMLHttpRequest.defaultRequestTime;
            //		if (typeof defaultRequestTime == "function") {
            //			defaultRequestTime = defaultRequestTime(query, options, event);
            //		}

            XMLHttpRequest.requestTime = options.requestTime || /*defaultRequestTime || */ DEFAULT_REQUEST_TIME;
            XMLHttpRequest.data = data || /*(event && event.srcElement.id) || */options.pollId;

            try {
                ajax("form", null, {});
            } finally {
                XMLHttpRequest.requestTime = undefined;
                XMLHttpRequest.data = undefined;
            }
        }
    }
}; 