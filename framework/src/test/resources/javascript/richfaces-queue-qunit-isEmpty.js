RichFaces.QUnit.run(function() {
    module("richfaces-queue-isEmpty");
    
    QUnit.testDone(function(context) {
        RichFaces.queue.clear();
    });
    
    // queue.isEmpty
    test("RichFaces.queue.isEmpty", function() {
        expect(2);
        
        var element = document.getElementById("testDiv");
        var event = {type:"testevent"};
        var options = {requestDelay:1000};
        var opts = {queueId:"myQueueIsEmpty", param:"value"};

        RichFaces.queue.setQueueOptions(opts.queueId, options);
        var size = RichFaces.queue.isEmpty();
        ok(RichFaces.queue.isEmpty(), "empty queue");
        jsf.ajax.request(element, event, opts);
        size = RichFaces.queue.isEmpty();
        ok(!RichFaces.queue.isEmpty(), "not empty");
    });
});