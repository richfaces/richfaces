RichFaces.QUnit.run(function() {
    module("richfaces-queue-submitFirst");

    QUnit.testDone(function(context) {
        RichFaces.queue.clear();
    });
    
    // queue.submitFirst
    test("RichFaces.queue.submitFirst", function() {
        expect(2);

        var element = document.getElementById("testDiv");
        var event = {type:"testevent"};
        var options = {requestDelay: 1000};
        var opts = {queueId:"myQueueSubmitFirst", param:"value"};
        
        RichFaces.queue.setQueueOptions(opts.queueId, options);
        jsf.ajax.request(element, event, opts);
        equal(RichFaces.queue.getSize(), 1, "one queueEntry is waiting");
        RichFaces.queue.submitFirst();
        equal(RichFaces.queue.getSize(), 0, "empty after submitFirst()");
    });

});