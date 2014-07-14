// Test for Jira issue RF-13706: dequeued Ajax request not processed correctly if its source element has been updated

RichFaces.QUnit.run(function() {
    module("richfaces-queue-remove-stale-entries");
    
    test("RichFaces.queue.remove-stale-entries", function() {
        expect(2);

        var testDivElement = document.getElementById("testDiv");
        RichFaces.QUnit.appendDomElements(testDivElement,
            '<form id="testForm">' +
                '<input id="button" type="button" value="hello" onclick="jsf.ajax.request(this,event,{queueId: \'testQueueId\', param: \'value\'}); return false;"/>' +
            '</form>');
        var button = document.getElementById("button");
        var numberOfAjaxResponses = 0;
        var numberOfAjaxRequests = 0;

        // Simulate that the Ajax response rerenders part of the page, causing the button to be removed from the DOM
        var simulateAjaxResponse = function(source) {
            numberOfAjaxResponses++;
            if (numberOfAjaxResponses == 1) {
                button.parentNode.removeChild(button);
            }
            for (var i = 0; i < jsf.ajax.eventHandlers.length; i++) {
                jsf.ajax.eventHandlers[i]({type: "event", status: "success"});
            }
        };

        // Simulate an Ajax request-response with a round trip time of 100 milliseconds
        RichFaces.ajaxContainer.jsfRequest = function (source, event, options) {
            numberOfAjaxRequests++;
            setTimeout(function() {
                simulateAjaxResponse(source)
            }, 100);
        }

        // Use a queue without delay, and the default requestGroupingID (causing grouping on the source of the request)
        RichFaces.queue.setQueueOptions({'testQueueId': {requestDelay: 0}});

        // Simulate a double click
        button.click({type: "onclick"});
        button.click({type: "onclick"});

        QUnit.stop();
        setTimeout(function() {
            // Let the test run for 500 milliseconds, and check that only one Ajax request is done.
            // If the Ajax request of the second click had not been removed, we would have had 2 requests and 2 responses.
            equal(numberOfAjaxRequests, 1);
            equal(numberOfAjaxResponses, 1);
            QUnit.start();
        }, 500);
    });
});
