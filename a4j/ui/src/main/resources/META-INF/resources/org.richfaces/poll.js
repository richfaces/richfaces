(function($, rf) {
    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};
    var defaultOptions = {
    };
    rf.rf4.ui.Poll = function(componentId, options) {
        $super.constructor.call(this, componentId, options);
        this.id = componentId;
        this.attachToDom();
        this.interval = options.interval || 1000;
        this.ontimer = options.ontimer;

        this.pollElement = rf.getDomElement(this.id);

        rf.rf4.ui.pollTracker = rf.rf4.ui.pollTracker || {};

        if (options.enabled) {
            this.startPoll();
        }
    }

    rf.BaseComponent.extend(rf.rf4.ui.Poll);
    var $super = rf.rf4.ui.Poll.$super;
    $.extend(rf.rf4.ui.Poll.prototype, (function() {
        return {
            name: "Poll",

            startPoll: function() {
                this.stopPoll();
                var poll = this;
                rf.rf4.ui.pollTracker[poll.id] = window.setTimeout(function() {
                    try {
                        poll.ontimer.call(poll.pollElement || window);
                        poll.startPoll();
                    } catch (e) {
                        // TODO: handle exception
                    }
                }, poll.interval);
            },

            stopPoll : function() {
                if (rf.rf4.ui.pollTracker && rf.rf4.ui.pollTracker[this.id]) {
                    window.clearTimeout(rf.rf4.ui.pollTracker[this.id]);
                    delete rf.rf4.ui.pollTracker[this.id];
                }
            },

            setZeroRequestDelay : function(options) {
                if (typeof options.requestDelay == "undefined") {
                    options.requestDelay = 0;
                }
            },

            destroy : function() {
                this.stopPoll();
                this.detach(this.id);
                // call parent's destroy method
                $super.destroy.call(this);
            }

        };
    })());

})(RichFaces.jQuery, RichFaces);