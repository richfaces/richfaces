(function($, rf) {
    rf.ui = rf.ui || {};
    var defaultOptions = {
    };
    rf.ui.Poll = function(componentId, options) {
        $super.constructor.call(this, componentId, options);
        this.id = componentId;
        this.attachToDom();
        this.interval = options.interval || 1000;
        this.ontimer = options.ontimer;

        this.pollElement = rf.getDomElement(this.id);

        rf.ui.pollTracker = rf.ui.pollTracker || {};

        if (options.enabled) {
            this.startPoll();
        }
    }

    rf.BaseComponent.extend(rf.ui.Poll);
    var $super = rf.ui.Poll.$super;
    $.extend(rf.ui.Poll.prototype, (function() {
        return {
            name: "Poll",

            startPoll: function() {
                this.stopPoll();
                var poll = this;
                rf.ui.pollTracker[poll.id] = window.setTimeout(function() {
                    try {
                        poll.ontimer.call(poll.pollElement || window);
                        poll.startPoll();
                    } catch (e) {
                        // TODO: handle exception
                    }
                }, poll.interval);
            },

            stopPoll : function() {
                if (rf.ui.pollTracker && rf.ui.pollTracker[this.id]) {
                    window.clearTimeout(rf.ui.pollTracker[this.id]);
                    delete rf.ui.pollTracker[this.id];
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

})(jQuery, RichFaces);