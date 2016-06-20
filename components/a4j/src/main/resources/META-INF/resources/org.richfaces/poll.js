(function($, rf) {
    rf.ui = rf.ui || {};
    var defaultOptions = {
    };
    
    /**
     * Backing object for a4j:poll
     * 
     * @extends RichFaces.BaseComponent
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.Poll
     * 
     * @param componentId
     * @param options
     */
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

            /**
             * Start the poll
             * 
             * @method
             * @name RichFaces.ui.Poll#startPoll
             */
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

            /**
             * Stop the poll
             * 
             * @method
             * @name RichFaces.ui.Poll#stopPoll
             */
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

})(RichFaces.jQuery, RichFaces);