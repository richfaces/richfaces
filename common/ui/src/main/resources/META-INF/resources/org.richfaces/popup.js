/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

    rf.ui.Popup = function(id, options) {
        this.id = id;
        this.popup = $(document.getElementById(id));

        var mergedOptions = $.extend({}, defaultOptions, options);
        this.visible = mergedOptions.visible;
        this.attachTo = mergedOptions.attachTo;
        this.attachToBody = mergedOptions.attachToBody;
        this.positionType = mergedOptions.positionType;
        this.positionOffset = mergedOptions.positionOffset;
    };

    rf.BaseComponent.extend(rf.ui.Popup);
    var $super = rf.ui.Popup.$super;


    var defaultOptions = {
        visible: false
    };

    $.extend(rf.ui.Popup.prototype, {

        name : "popup",

        show: function(event) {
            if (!this.visible) {
                if (this.attachToBody) {
                    this.parentElement = this.popup.parent();
                    this.popup.detach().appendTo("body");
                }
                this.visible = true;
            }

            this.popup.setPosition(event || {id: this.attachTo}, {type: this.positionType , offset: this.positionOffset}).show();
        },

        hide: function() {
            if (this.visible) {
                this.popup.hide();
                this.visible = false;
                if (this.attachToBody && this.parentElement) {
                    this.popup.detach().appendTo(this.parentElement);
                    this.parentElement = null;
                }
            }
        },

        isVisible: function() {
            return this.visible;
        },

        getId: function() {
            return this.id;
        }
    });

})(jQuery, window.RichFaces);