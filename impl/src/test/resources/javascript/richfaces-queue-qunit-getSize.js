RichFaces.QUnit.run(function() {
    module("richfaces-queue-getSize");

    var element = document.getElementById("testDiv");
    var event = {type:"testevent"};
    var options = {requestDelay:1000};
    var opts = {queueId:"myQueue", param:"value"};

    // queue.getSize
    test("RichFaces.queue.getSize", function() {
        expect(2);
        RichFaces.queue.setQueueOptions(opts.queueId, options);
        var size = RichFaces.queue.getSize();
        equals(size, 0, "empty queue");
        jsf.ajax.request(element, event, opts);
        size = RichFaces.queue.getSize();
        equals(size, 1, "not empty");
    });
});