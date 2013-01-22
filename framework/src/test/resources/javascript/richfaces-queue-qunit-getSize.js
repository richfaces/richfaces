RichFaces.QUnit.run(function() {
    module("richfaces-queue-getSize");

    QUnit.testDone(function(context) {
        RichFaces.queue.clear();
    });
    
    // queue.getSize
    test("RichFaces.queue.getSize", function() {
        expect(2);
        
        var element = document.getElementById("testDiv");
        var event = {type:"testevent"};
        var options = {requestDelay:1000};
        var opts = {queueId:"myQueueGetSize", param:"value"};
        
        RichFaces.queue.setQueueOptions(opts.queueId, options);
        var size = RichFaces.queue.getSize();
        equal(size, 0, "empty queue");
        jsf.ajax.request(element, event, opts);
        size = RichFaces.queue.getSize();
        equal(size, 1, "not empty");
    });
});