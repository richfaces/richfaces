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

(function ($, rf) {

    rf.ui = rf.ui || {};

    var initButtons = function(buttons, css, component) {
        var id;

        var fn = function(e) {
            e.data.fn.call(e.data.component, e);
        };

        var data = {};
        data.component = component;

        for (id in buttons) {
            var element = $(document.getElementById(id));

            data.id = id;
            data.page = buttons[id];
            data.element = element;
            data.fn = component.processClick;

            element.bind('click', copy(data), fn);
        }
    };

    var copy = function(data) {
        var key;
        var eventData = {};

        for (key in data) {
            eventData[key] = data[key];
        }

        return eventData;
    };

    var togglePressClass = function(el, event) {
        if (event.type == 'mousedown') {
            el.addClass('rf-ds-press');
        } else if (event.type == 'mouseup' || event.type == 'mouseout') {
            el.removeClass('rf-ds-press');
        }
    };

    rf.ui.DataScroller = function(id, submit, options) {

        $super.constructor.call(this, id);

        var dataScrollerElement = this.attachToDom();

        this.options = options;
        this.currentPage = options.currentPage;

        if (submit && typeof submit == 'function') {
            rf.Event.bindById(id, this.getScrollEventName(), submit);
        }

        var css = {};

        if (options.buttons) {

            $(dataScrollerElement).delegate('.rf-ds-btn', 'mouseup mousedown mouseout', function(event) {
                if ($(this).hasClass('rf-ds-dis')) {
                    $(this).removeClass('rf-ds-press');
                } else {
                    togglePressClass($(this), event);
                }
            });

            initButtons(options.buttons.left, css, this);
            initButtons(options.buttons.right, css, this);
        }

        if (options.digitals) {

            $(dataScrollerElement).delegate('.rf-ds-nmb-btn', 'mouseup mousedown mouseout', function(event) {
                togglePressClass($(this), event);
            });

            initButtons(options.digitals, css, this);
        }
    };

    rf.BaseComponent.extend(rf.ui.DataScroller);
    var $super = rf.ui.DataScroller.$super;

    $.extend(rf.ui.DataScroller.prototype, (function () {

        var scrollEventName = "r:datascroller:onscroll";

        return {

            name: "RichFaces.ui.DataScroller",

            processClick: function(event) {
                var data = event.data;
                if (data) {
                    var page = data.page;
                    if (page) {
                        this.switchToPage(page);
                    }
                }
            },

            switchToPage: function(page) {
                if (typeof page != 'undefined' && page != null) {
                    rf.Event.fireById(this.id, this.getScrollEventName(), {'page' : page});
                }
            },

            fastForward: function() {
                this.switchToPage("fastforward");
            },

            fastRewind: function() {
                this.switchToPage("fastrewind");
            },

            next: function() {
                this.switchToPage("next");
            },

            previous: function() {
                this.switchToPage("previous");
            },

            first: function() {
                this.switchToPage("first");
            },

            last: function() {
                this.switchToPage("last");
            },

            getScrollEventName: function() {
                return scrollEventName;
            },
            destroy: function() {
                $super.destroy.call(this);
            }
        }

    })());

})(RichFaces.jQuery, RichFaces);