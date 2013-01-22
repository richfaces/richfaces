if (typeof jsf != 'undefined') {
    (function(jQuery, richfaces, jsf) {

        //JSF log adapter
        var identifyElement = function(elt) {
            var identifier = '<' + elt.tagName.toLowerCase();
            var e = jQuery(elt);
            if (e.attr('id')) {
                identifier += (' id=' + e.attr('id'));
            }
            if (e.attr('class')) {
                identifier += (' class=' + e.attr('class'));
            }

            identifier += ' ...>';

            return identifier;
        }

        var formatPartialResponseElement = function(logElement, responseElement) {
            var change = jQuery(responseElement);

            logElement.append("Element <b>" + responseElement.nodeName + "</b>");
            if (change.attr("id")) {
                logElement.append(document.createTextNode(" for id=" + change.attr("id")));
            }

            jQuery(document.createElement("br")).appendTo(logElement);
            jQuery("<span style='color:dimgray'></span>").appendTo(logElement).text(change.toXML());
            jQuery(document.createElement("br")).appendTo(logElement);
        }

        var formatPartialResponse = function(partialResponse) {
            var logElement = jQuery(document.createElement("span"));

            partialResponse.children().each(function() {
                var responseElement = jQuery(this);
                if (responseElement.is('changes')) {
                    logElement.append("Listing content of response <b>changes</b> element:<br />");
                    responseElement.children().each(function() {
                        formatPartialResponseElement(logElement, this);
                    });
                } else {
                    formatPartialResponseElement(logElement, this);
                }
            });

            return logElement;
        }

        var jsfAjaxLogAdapter = function(data) {
            try {
                var log = richfaces.log;

                var source = data.source;
                var type = data.type;

                var responseCode = data.responseCode;
                var responseXML = data.responseXML;
                var responseText = data.responseText;

                if (type != 'error') {
                    log.info("Received '" + type + "' event from " + identifyElement(source));

                    if (type == 'beforedomupdate') {
                        var partialResponse;

                        if (responseXML) {
                            partialResponse = jQuery(responseXML).children("partial-response");
                        }

                        var responseTextEntry = jQuery("<span>Server returned responseText: </span><span style='color:dimgray'></span>").eq(1).text(responseText).end();

                        if (partialResponse && partialResponse.length) {
                            log.debug(responseTextEntry);
                            log.info(formatPartialResponse(partialResponse));
                        } else {
                            log.info(responseTextEntry);
                        }
                    }
                } else {
                    var status = data.status;
                    log.error("Received '" + type + '@' + status + "' event from " + identifyElement(source));
                    log.error("[" + data.responseCode + "] " + data.errorName + ": " + data.errorMessage);
                }
            } catch (e) {
                //ignore logging errors
            }
        };

        var eventsListener = richfaces.createJSFEventsAdapter({
                begin: jsfAjaxLogAdapter,
                beforedomupdate: jsfAjaxLogAdapter,
                success: jsfAjaxLogAdapter,
                complete: jsfAjaxLogAdapter,
                error: jsfAjaxLogAdapter
            });

        jsf.ajax.addOnEvent(eventsListener);
        jsf.ajax.addOnError(eventsListener);
        //
    }(jQuery, RichFaces, jsf));
}
;