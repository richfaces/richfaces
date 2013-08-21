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

(function ($, richfaces) {

    richfaces.ui = richfaces.ui || {};

    richfaces.ui.CollapsibleSubTableToggler = function(id, options) {
        this.id = id;
        this.eventName = options.eventName;
        this.expandedControl = options.expandedControl;
        this.collapsedControl = options.collapsedControl;
        this.forId = options.forId;
        this.element = $(document.getElementById(this.id));

        if (this.element && this.eventName) {
            this.element.bind(this.eventName, $.proxy(this.switchState, this));
        }
    };

    $.extend(richfaces.ui.CollapsibleSubTableToggler.prototype, (function () {

        var getElementById = function(id) {
            return $(document.getElementById(id))
        }

        return {

            switchState: function(e) {
                var subtable = richfaces.component(this.forId);
                if (subtable) {
                    var mode = subtable.getMode();

                    if (richfaces.ui.CollapsibleSubTable.MODE_CLNT == mode) {
                        this.toggleControl(subtable.isExpanded());
                    }

                    subtable.setOption(this.id);
                    subtable.switchState(e);
                }
            },

            toggleControl: function(collapse) {
                var expandedControl = getElementById(this.expandedControl);
                var collapsedControl = getElementById(this.collapsedControl);

                if (collapse) {
                    expandedControl.hide();
                    collapsedControl.show();
                } else {
                    collapsedControl.hide();
                    expandedControl.show();
                }
            }
        };
    })());

})(jQuery, window.RichFaces);