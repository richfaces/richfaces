var Timer = {

    _eventCounter: 0,

    currentTime: 0,

    maxTime: 10000000,

    events: new Array(),

    wSetTimeout: window.setTimeout,

    wClearTimeout: window.clearTimeout,

    addEventToTimer: function(callback, delay) {
        var eventTime = this.currentTime + delay;

        var i = 0;

        while (this.events[i] && (this.events[i].eventTime <= eventTime)) {
            i++;
        }

        var eventId = this._eventCounter++;

        this.events.splice(i, 0, {eventTime: eventTime, callback: callback, eventId: eventId});

        return eventId;
    },

    removeEventFromTimer: function(eventId) {
        for (var i = 0; i < this.events.length; i++) {
            if (this.events[i].eventId == eventId) {
                this.events.splice(i, 1);

                break;
            }
        }
    },

    execute: function() {
        while (this.events.length > 0) {

            var eventData = this.events.shift();

            this.currentTime = eventData.eventTime;
            if (this.currentTime > this.maxTime) {
                throw "Maximum execution time reached, aborting timer";
            }

            try {
                eventData.callback();
            } catch (e) {
                alert(e.message);
            }
        }
    },

    isEmpty: function() {
        return this.events.length == 0;
    },

    clear: function() {
        this._eventCounter = 0;
        this.currentTime = 0;
        this.events = [];
    },

    beginSimulation: function() {
        this.clear();
        window.setTimeout = document.setTimeout = function(callback, delay) {
            return Timer.addEventToTimer(callback, delay);
        };

        window.clearTimeout = document.clearTimeout = function(timerId) {
            Timer.removeEventFromTimer(timerId);
        };
    },

    endSimulation: function() {
        window.setTimeout = Timer.wSetTimeout;
        window.clearTimeout = Timer.wClearTimeout;
    }
};