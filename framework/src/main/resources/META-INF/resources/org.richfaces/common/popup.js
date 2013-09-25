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

    rf.ui.Popup = function(id, options) {
        $super.constructor.call(this, id);
        this.options = $.extend({}, defaultOptions, options);
        this.positionOptions = {type: this.options.positionType, from:this.options.jointPoint, to:this.options.direction, offset: this.options.positionOffset};

        this.popup = $(document.getElementById(id));

        this.visible = this.options.visible;
        this.attachTo = this.options.attachTo;
        this.attachToBody = this.options.attachToBody;
        this.positionType = this.options.positionType;
        this.positionOffset = this.options.positionOffset;
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
                        this.parentElement = this.popup.parent().get(0);
                        document.body.appendChild(this.popup.get(0));
                    }
                    this.visible = true;
                }

                this.popup.setPosition(event || {id: this.attachTo}, this.positionOptions).show();
            },

            hide: function() {
                if (this.visible) {
                    this.popup.hide();
                    this.visible = false;
                    if (this.attachToBody && this.parentElement) {
                        this.parentElement.appendChild(this.popup.get(0));
                        this.parentElement = null;
                    }
                }
            },

            isVisible: function() {
                return this.visible;
            },

            getId: function() {
                return this.id;
            },

            destroy: function() {
                if (this.attachToBody && this.parentElement) {
                    this.parentElement.appendChild(this.popup.get(0));
                    this.parentElement = null;
                }
            }
        });

})(RichFaces.jQuery, RichFaces);