/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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