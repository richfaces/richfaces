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

})(RichFaces.jQuery, RichFaces);