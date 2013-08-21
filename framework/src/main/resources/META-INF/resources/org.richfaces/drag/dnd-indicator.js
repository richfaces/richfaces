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

/*
 * code review by Pavel Yaschenko
 * 
 * 1. No need to save DOM element (this.indicator). We can use id to get dom element. It helps to avoid memory leaks :)
 * 
 * 2. Name refactoring: change names acceptClass, rejectClass, draggingClass 
 * 						to more readable names: getAcceptClass, getRejectClass, getDragClass
 * 
 */

(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.DragIndicator = function(id, options) {
        $super.constructor.call(this, id);
        this.attachToDom(id);

        this.indicator = $(document.getElementById(id));
        this.options = options;
    };

    var defaultOptions = {
    };

    rf.BaseComponent.extend(rf.ui.DragIndicator);
    var $super = rf.ui.DragIndicator.$super;

    $.extend(rf.ui.DragIndicator.prototype, ( function () {
        return {
            show : function() {
                this.indicator.show();
            },

            hide: function() {
                this.indicator.hide();
            },

            getAcceptClass: function() {
                return this.options.acceptClass;
            },

            getRejectClass: function() {
                return this.options.rejectClass;
            },

            getDraggingClass: function() {
                return this.options.draggingClass;
            },

            getElement: function() {
                return this.indicator;
            }
        }
    })());

})(RichFaces.jQuery, RichFaces);

