RichFaces.QUnit.run(function() {
    module("richfaces-queue-isEmpty");

    var element = document.getElementById("testDiv");
    var event = {type:"testevent"};
    var options = {requestDelay:1000};
    var opts = {queueId:"myQueue", param:"value"};

    // queue.isEmpty
    test("RichFaces.queue.isEmpty", function() {
        expect(2);
        RichFaces.queue.setQueueOptions(opts.queueId, options);
        var size = RichFaces.queue.isEmpty();
        ok(RichFaces.queue.isEmpty(), "empty queue");
        jsf.ajax.request(element, event, opts);
        size = RichFaces.queue.isEmpty();
        ok(!RichFaces.queue.isEmpty(), "not empty");
    });
});