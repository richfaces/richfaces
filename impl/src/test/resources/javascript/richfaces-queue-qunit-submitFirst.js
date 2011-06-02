RichFaces.QUnit.run(function() {
    module("richfaces-queue-submitFirst");

    var element = document.getElementById("testDiv");
    var event = {type:"testevent"};
    var options = {requestDelay: 1000};
    var opts = {queueId:"myQueue", param:"value"};

    // queue.submitFirst
    test("RichFaces.queue.submitFirst", function() {
        expect(2);
        RichFaces.queue.setQueueOptions(opts.queueId, options);
        jsf.ajax.request(element, event, opts);
        equals(RichFaces.queue.getSize(), 1, "one queueEntry is waiting");
        RichFaces.queue.submitFirst();
        equals(RichFaces.queue.getSize(), 0, "empty after submitFirst()");
    });

});