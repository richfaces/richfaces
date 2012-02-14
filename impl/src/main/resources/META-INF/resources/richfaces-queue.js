/**
 * @author Pavel Yaschenko
 */

(function($, richfaces, jsf) {

    /**
     * RichFaces Ajax container
     * @class
     * @memberOf RichFaces
     * @static
     * @name ajaxContainer
     * */
    richfaces.ajaxContainer = richfaces.ajaxContainer || {};

    if (richfaces.ajaxContainer.jsfRequest) {
        return;
    }

    /**
     * JSF 2.0 original method that sends an asynchronous ajax request to the server
     * see jsf.ajax.request method for parameter's description
     * @function
     * @name RichFaces.ajaxContainer.jsfRequest
     *
     * */
    richfaces.ajaxContainer.jsfRequest = jsf.ajax.request;

    /**
     * RichFaces wrapper function of JSF 2.0 original method jsf.ajax.request
     * @function
     * @name jsf.ajax.request
     *
     * @param {string|DOMElement} source - The DOM element or an id that triggered this ajax request
     * @param {object} [event] - The DOM event that triggered this ajax request
     * @param {object} [options] - The set name/value pairs that can be sent as request parameters to control client and/or server side request processing
     * */
    jsf.ajax.request = function(source, event, options) {
        richfaces.queue.push(source, event, options);
    };

    richfaces.ajaxContainer.jsfResponse = jsf.ajax.response;

    richfaces.ajaxContainer.isIgnoreResponse = function() {
        return richfaces.queue.isIgnoreResponse();
    };


    jsf.ajax.response = function(request, context) {
        richfaces.queue.response(request, context);
    };

    var QUEUE_MODE_PULL = 'pull';
    var QUEUE_MODE_PUSH = 'push';
    var QUEUE_MODE = QUEUE_MODE_PULL;
    var DEFAULT_QUEUE_ID = "org.richfaces.queue.global";

    /**
     * RichFaces Queue API container
     * @class
     * @memberOf RichFaces
     * @static
     * @name queue
     * */
    richfaces.queue = (function() {

        var defaultQueueOptions = {};
        //defaultQueueOptions[DEFAULT_QUEUE_ID] = {requestDelay:0, ignoreDupResponse:false, timeout:0};
        var eventHandlers = {};

        var QueueEntry = function(queue, source, event, options) {
            this.queue = queue;
            this.source = source;
            this.options = $.extend({}, options || {});
            this.queueOptions = {}
            var id;

            // find default options for QueueEntry
            if (this.options.queueId) {
                if (defaultQueueOptions[this.options.queueId]) {
                    id = this.options.queueId;
                }
                delete this.options.queueId;
            } else {
                var element = richfaces.getDomElement(source);
                var form;
                if (element) {
                    element = $(element).closest("form");
                    if (element.length > 0) {
                        form = element.get(0);
                    }
                }
                if (form && form.id && defaultQueueOptions[form.id]) {
                    id = form.id;
                } else {
                    id = DEFAULT_QUEUE_ID;
                }
            }
            if (id) {
                this.queueOptions = defaultQueueOptions[id] || {};
                if (this.queueOptions.queueId) {
                    this.queueOptions = $.extend({}, (defaultQueueOptions[this.queueOptions.queueId] || {}), this.queueOptions);
                } else {
                    // TODO: clean duplicated code
                    var element = richfaces.getDomElement(source);
                    var form;
                    if (element) {
                        element = $(element).closest("form");
                        if (element.length > 0) {
                            form = element.get(0);
                        }
                    }
                    if (form && form.id && defaultQueueOptions[form.id]) {
                        id = form.id;
                    } else {
                        id = DEFAULT_QUEUE_ID;
                    }
                    if (id) {
                        this.queueOptions = $.extend({}, (defaultQueueOptions[id] || {}), this.queueOptions);
                    }
                }
            }

            if (typeof this.queueOptions.requestGroupingId == "undefined") {
                this.queueOptions.requestGroupingId = typeof this.source == "string" ? this.source : this.source.id;
            }

            // Remove the layerX and layerY events (generated in WebKit browsers)
            if (event) {
                if ('layerX' in event) delete event.layerX;
                if ('layerY' in event) delete event.layerY;
            }

            // copy of event should be created otherwise IE will fail
            this.event = $.extend({}, event);

            //requestGroupingId is mutable, thus we need special field for it
            this.requestGroupingId = this.queueOptions.requestGroupingId;
            this.eventsCount = 1;
        };

        $.extend(QueueEntry.prototype, {
                // now unused functions: ondrop, clearEntry
                isIgnoreDupResponses: function() {
                    return this.queueOptions.ignoreDupResponses;
                },

                getRequestGroupId: function() {
                    return this.requestGroupingId;
                },

                setRequestGroupId: function(id) {
                    this.requestGroupingId = id;
                },

                resetRequestGroupId: function() {
                    this.requestGroupingId = undefined;
                },

                setReadyToSubmit: function(isReady) {
                    this.readyToSubmit = isReady;
                },

                getReadyToSubmit: function() {
                    return this.readyToSubmit;
                },

                ondrop: function() {
                    var callback = this.queueOptions.onqueuerequestdrop;
                    if (callback) {
                        callback.call(this.queue, this.source, this.options, this.event);
                    }
                },

                onRequestDelayPassed: function() {
                    this.readyToSubmit = true;
                    submitFirstEntry.call(this.queue);
                },

                startTimer: function() {
                    var delay = this.queueOptions.requestDelay;
                    if (typeof delay != "number") {
                        delay = this.queueOptions.requestDelay || 0;
                    }

                    log.debug("Queue will wait " + (delay || 0) + "ms before submit");

                    if (delay) {
                        var _this = this;
                        this.timer = window.setTimeout(function() {
                            try {
                                _this.onRequestDelayPassed();
                            } finally {
                                _this.timer = undefined;
                                _this = undefined;
                            }
                        }, delay);
                    } else {
                        this.onRequestDelayPassed();
                    }
                },

                stopTimer: function() {
                    if (this.timer) {
                        window.clearTimeout(this.timer);
                        this.timer = undefined;
                    }
                },

                clearEntry: function() { //???
                    this.stopTimer();
                    if (this.request) {
                        this.request.shouldNotifyQueue = false;
                        this.request = undefined;
                    }
                },

                getEventsCount: function() {
                    return this.eventsCount;
                },

                setEventsCount: function(newCount) {
                    this.eventsCount = newCount;
                }
            });

        // TODO: add this two variables to richfaces and report bug to jsf about constants
        var JSF_EVENT_TYPE = 'event';
        var JSF_EVENT_SUCCESS = 'success';
        var JSF_EVENT_COMPLETE = 'complete';

        var log = richfaces.log;
        var items = [];
        var lastRequestedEntry;

        //TODO: instance of this function will be created for each queue
        var onError = function (data) {
            log.debug("richfaces.queue: ajax submit error");
            lastRequestedEntry = null;
            //TODO: what if somebody is going to clear queue on error?
            submitFirstEntry();
        };

        var onComplete = function (data) {
            if (data.type == JSF_EVENT_TYPE && data.status == JSF_EVENT_SUCCESS) { // or JSF_EVENT_COMPLETE will be rather
                log.debug("richfaces.queue: ajax submit successfull");
                lastRequestedEntry = null;
                submitFirstEntry();
            }
        };

        jsf.ajax.addOnEvent(onComplete);
        jsf.ajax.addOnError(onError);

        var submitFirstEntry = function() {
            if (QUEUE_MODE == QUEUE_MODE_PULL && lastRequestedEntry) {
                log.debug("richfaces.queue: Waiting for previous submit results");
                return;
            }
            if (isEmpty()) {
                log.debug("richfaces.queue: Nothing to submit");
                return;
            }
            var entry;
            if (items[0].getReadyToSubmit()) {
                entry = lastRequestedEntry = items.shift();
                log.debug("richfaces.queue: will submit request NOW");
                var o = lastRequestedEntry.options;
                o["AJAX:EVENTS_COUNT"] = lastRequestedEntry.eventsCount;
                richfaces.ajaxContainer.jsfRequest(lastRequestedEntry.source, lastRequestedEntry.event, o);

                // call event handlers
                if (o.queueonsubmit) {
                    o.queueonsubmit.call(entry);
                }
                callEventHandler("onrequestdequeue", entry);
            }
        };

        var isEmpty = function() {
            return (getSize() == 0)
        };
        var getSize = function() {
            return items.length;
        };

        var getLastEntry = function () {
            var lastIdx = items.length - 1;
            return items[lastIdx];
        };

        var updateLastEntry = function (entry) {
            var lastIdx = items.length - 1;
            items[lastIdx] = entry;
        };

        var callEventHandler = function (handlerName, entry) {
            var handler = entry.queueOptions[handlerName];
            if (handler) {
                if (typeof(handler) == "string") {
                    new Function(handler).call(null, entry);
                } else {
                    handler.call(null, entry);
                }
            }
            var opts, handler2;
            if (entry.queueOptions.queueId &&
                (opts = defaultQueueOptions[entry.queueOptions.queueId]) &&
                (handler2 = opts[handlerName])
                && handler2 != handler) {
                // the same about context
                handler2.call(null, entry);
            }
        }

        var pushEntry = function (entry) {
            items.push(entry);
            log.debug("New request added to queue. Queue requestGroupingId changed to " + entry.getRequestGroupId());
            // call event handlers
            callEventHandler("onrequestqueue", entry);
        }

        return {
            /**
             * @constant
             * @name RichFaces.queue.DEFAULT_QUEUE_ID
             * @type string
             * */
            DEFAULT_QUEUE_ID: DEFAULT_QUEUE_ID,

            /**
             * Get current queue size
             * @function
             * @name RichFaces.queue.getSize
             *
             * @return {number} size of items in the queue
             * */
            getSize: getSize,

            /**
             * Check if queue is empty
             * @function
             * @name RichFaces.queue.isEmpty
             *
             * @return {boolean} returns true if queue is empty
             * */
            isEmpty: isEmpty,

            /**
             * Extract and submit first QueueEntry in the queue if QueueEntry is ready to submit
             * @function
             * @name RichFaces.queue.submitFirst
             * */
            submitFirst: function () {
                if (!isEmpty()) {
                    var entry = items[0];
                    entry.stopTimer();
                    entry.setReadyToSubmit(true);
                    submitFirstEntry();
                }
            },

            /**
             * Create and push QueueEntry to the queue for ajax requests
             * @function
             * @name RichFaces.queue.push
             *
             * @param {string|DOMElement} source - The DOM element or an id that triggered this ajax request
             * @param {object} [event] - The DOM event that triggered this ajax request
             * @param {object} [options] - The set name/value pairs that can be sent as request parameters to control client and/or server side request processing
             * */
            push: function (source, event, options) {
                var entry = new QueueEntry(this, source, event, options);
                var requestGroupingId = entry.getRequestGroupId();

                var lastEntry = getLastEntry();

                if (lastEntry) {
                    if (lastEntry.getRequestGroupId() == requestGroupingId) {
                        log.debug("Similar request currently in queue");

                        log.debug("Combine similar requests and reset timer");

                        lastEntry.stopTimer();
                        entry.setEventsCount(lastEntry.getEventsCount() + 1);

                        updateLastEntry(entry);
                        callEventHandler("onrequestqueue", entry);
                    } else {
                        log.debug("Last queue entry is not the last anymore. Stopping requestDelay timer and marking entry as ready for submission")

                        lastEntry.stopTimer();
                        lastEntry.resetRequestGroupId();
                        lastEntry.setReadyToSubmit(true);

                        pushEntry(entry);
                        submitFirstEntry();
                    }
                } else {
                    pushEntry(entry);
                }

                // start timer
                entry.startTimer();

            },

            response: function (request, context) {
                if (this.isIgnoreResponse()) {
                    lastRequestedEntry = null;
                    submitFirstEntry();
                } else {
                    richfaces.ajaxContainer.jsfResponse(request, context);
                }
            },

            isIgnoreResponse: function () {
                var entry = items[0];
                return entry && lastRequestedEntry.isIgnoreDupResponses()
                    && lastRequestedEntry.queueOptions.requestGroupingId == entry.queueOptions.requestGroupingId;
            },

            /**
             * Remove all QueueEntry from the queue
             * @function
             * @name RichFaces.queue.clear
             * */
            clear: function () {
                var lastEntry = getLastEntry();
                if (lastEntry) {
                    lastEntry.stopTimer();
                }
                items = [];
            },

            /**
             * Set queue default options
             * @function
             * @name RichFaces.queue.setQueueOptions
             *
             * @param {string||object} [id] - Queue id for storing options or hash with options for multiple options set
             * @param {object} options - Queue options object
             * */
            setQueueOptions: function (id, options) {
                var tid = typeof id;
                if (tid == "string") {
                    // add named queue options
                    if (defaultQueueOptions[id]) {
                        throw "Queue already registered";
                    } else {
                        defaultQueueOptions[id] = options;
                    }
                } else if (tid == "object") {
                    // first parameter is hash with queue names and options
                    $.extend(defaultQueueOptions, id);
                }
                return richfaces.queue;
            },

            getQueueOptions: function (id) {
                return defaultQueueOptions[id] || {};
            }
        }
    }());
}(jQuery, RichFaces, jsf));