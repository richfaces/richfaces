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

    rf.ui.AccordionItem = rf.ui.TogglePanelItem.extendClass({
        // class name
        name:"AccordionItem",

        /**
         * @class AccordionItem
         * @name AccordionItem
         *
         * @constructor
         * @param {String} componentId - component id
         * @param {Hash} options - params
         * */
        init : function (componentId, options) {
            rf.ui.TogglePanelItem.call(this, componentId, options);
            this.index = options["index"];
            this.getTogglePanel().getItems()[this.index] = this;

            rf.Event.bindById(this.id + ":header", "click", this.__onHeaderClick, this)
        },

        /***************************** Public Methods  ****************************************************************/

        __onHeaderClick : function (comp) {
            this.getTogglePanel().switchToItem(this.getName());
        },

        /**
         * @return {DOMElement}
         * */
        __header : function () {
            return rf.getDomElement(this.id + ":header");
        },

        /**
         * @return {DOMElement}
         * */
        __content : function () {
            return rf.getDomElement(this.id + ":content");
        },

        /**
         * @private
         *
         * used in TogglePanel
         * */
        __enter : function () {
            var parentPanel = this.getTogglePanel();
            if (parentPanel.isKeepHeight) {
                var h = parentPanel.getInnerHeight();

                var items = parentPanel.getItems();
                for (var i = 0; i < items.length; i++) {
                    h -= items[i].getHeight();
                }

                $(this.__content()).height(h);
            }

            this.__content().style.display = "block";


            return this.__fireEnter();
        },

        getHeight : function (recalculate) {
            if (recalculate || !this.__height) {
                this.__height = $(rf.getDomElement(this.id)).outerHeight(true)
            }

            return this.__height;
        },

        /**
         * @private
         *
         * used in TogglePanel
         * */
        __leave : function () {
            var continueProcess = this.__fireLeave();
            if (!continueProcess) {
                return false;
            }

            this.__content().style.display = "none";
            return true;
        },

        /***************************** Private Methods ********************************************************/


        destroy: function () {
            var parent = this.getTogglePanel();
            delete parent.getItems()[this.index];

            rf.Event.unbindById(this.id, "."+this.namespace);

            this.$super.destroy.call(this);
        }
    });
})(jQuery, RichFaces);
