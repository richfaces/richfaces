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

window.RichFaces = window.RichFaces || {};
RichFaces.jQuery = RichFaces.jQuery || window.jQuery;

(function($, rf) {

    // Constructor definition
    rf.ui.Message = function(componentId, options) {
        // call constructor of parent class
        $super.constructor.call(this, componentId, options, defaultOptions);
        if (this.options.isMessages) {
            this.severityClasses = ["rf-msgs-inf", "rf-msgs-wrn", "rf-msgs-err", "rf-msgs-ftl"];
            this.summaryClass = "rf-msgs-sum";
            this.detailClass = "rf-msgs-det";
        } else {
            this.severityClasses = ["rf-msg-inf", "rf-msg-wrn", "rf-msg-err", "rf-msg-ftl"];
            this.summaryClass = "rf-msg-sum";
            this.detailClass = "rf-msg-det";
        }
    };

    // Extend component class and add protected methods from parent class to our container
    rf.ui.Base.extend(rf.ui.Message);

    // define super class link
    var $super = rf.ui.Message.$super;

    var defaultOptions = {
        showSummary:true,
        level:0,
        isMessages: false,
        globalOnly: false
    };


    var onMessage = function (event, element, data) {
        var content = $(rf.getDomElement(this.id));
        var sourceId = data.sourceId;
        var message = data.message;
        if (!this.options.forComponentId) {
            if (!message || this.options.globalOnly) {
                // rf.csv.clearMessage
                var element;
                while (element = rf.getDomElement(this.id + ':' + sourceId)) {
                    $(element).remove();
                }
            } else {
                renderMessage.call(this, sourceId, message);
            }
        } else if (this.options.forComponentId === sourceId) {
            content.empty();
            renderMessage.call(this, sourceId, message);
        }
    }

    var renderMessage = function(index, message) {
        if (message && message.severity >= this.options.level) {

            var content = $(rf.getDomElement(this.id));
            var msgContent = $("<span/>", {'class':(this.severityClasses)[message.severity],"id":this.id + ':' + index});
            if (message.summary) {
                if (this.options.tooltip) {
                    msgContent.attr("title", message.summary);
                } else if (this.options.showSummary) {
                    msgContent.append($("<span/>", {"class":(this.summaryClass)}).text(message.summary));
                }
            }
            if (this.options.showDetail && message.detail) {
                msgContent.append($("<span/>", {"class":(this.detailClass)}).text(message.detail));
            }
            content.append(msgContent);
        }
    }

    var bindEventHandlers = function () {
        rf.Event.bind(window.document, rf.Event.MESSAGE_EVENT_TYPE + this.namespace, onMessage, this);
    };

    $.extend(rf.ui.Message.prototype, {
            name: "Message",
            __bindEventHandlers: bindEventHandlers,
            
            destroy : function() {
                rf.Event.unbind(window.document, rf.Event.MESSAGE_EVENT_TYPE + this.namespace);
                $super.destroy.call(this);
            }
        });

})(RichFaces.jQuery, RichFaces);
