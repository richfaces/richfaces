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

    rf.ui.ListMulti = function(id, options) {
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, id);
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        this.disabled = mergedOptions.disabled;
    };

    rf.ui.List.extend(rf.ui.ListMulti);
    var $super = rf.ui.ListMulti.$super;

    var defaultOptions = {
        clickRequiredToSelect: true
    };

    $.extend(rf.ui.ListMulti.prototype, ( function () {

        return{

            name : "listMulti",

            getSelectedItems: function() {
                return this.list.find("." + this.selectItemCssMarker);
            },

            removeSelectedItems: function() {
                var items = this.getSelectedItems();
                this.removeItems(items);
                return items;
            },

            __selectByIndex: function(index, clickModified) {
                if (! this.__isSelectByIndexValid(index)) {
                    return;
                }

                this.index = this.__sanitizeSelectedIndex(index);

                var item = this.items.eq(this.index);
                if (! clickModified) {
                    var that = this;
                    this.getSelectedItems().each( function() {
                        that.unselectItem($(this))
                    });
                    this.selectItem(item);
                } else {
                    if (this.isSelected(item)) {
                        this.unselectItem(item);
                    } else {
                        this.selectItem(item);
                    }
                }
            }
        }
    })());

})(RichFaces.jQuery, RichFaces);