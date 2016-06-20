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

    rf.ui.CollapsiblePanel = rf.ui.TogglePanel.extendClass({

            name:"CollapsiblePanel",

            /**
             * Backing object for rich:collapsiblePanel
             * 
             * @extends RichFaces.ui.TogglePanel
             * @memberOf! RichFaces.ui
             * @constructs RichFaces.ui.CollapsiblePanel
             * 
             * @param {string} componentId - component id
             * @param {Object} options - params
             * */
            init : function (componentId, options) {
                rf.ui.TogglePanel.call(this, componentId, options);
                this.switchMode = options.switchMode;

                this.__addUserEventHandler("beforeswitch");
                this.__addUserEventHandler("switch");

                this.options.cycledSwitching = true;

                var panel = this;
                $(document.getElementById(this.id)).ready(function () { // TODO
                    rf.Event.bindById(panel.id + ":header", "click", panel.__onHeaderClick, panel);

                    new RichFaces.ui.CollapsiblePanelItem(
                        panel.id + ":content", {"index":0, "togglePanelId":panel.id, "switchMode":panel.switchMode, "name":"true"}),

                        new RichFaces.ui.CollapsiblePanelItem(
                            panel.id + ":empty", {"index":1, "togglePanelId":panel.id, "switchMode":panel.switchMode, "name":"false"})
                })
            },

            /**
             * Switch the state of the panel
             * 
             * @method
             * @name RichFaces.ui.CollapsiblePanel#switchPanel
             * @param [to] {string} state to switch to: "true" (expanded), "false" (collapsed), "@next"
             */
            switchPanel : function (to) {
                this.switchToItem(to || "@next");
            },

            /**
             * Expand the panel
             * 
             * @method
             * @name RichFaces.ui.CollapsiblePanel#expand
             */
            expand: function() {
                this.switchToItem("true");
            },

            /**
             * Collapse the panel
             * 
             * @method
             * @name RichFaces.ui.CollapsiblePanel#collapse
             */
            collapse: function() {
                this.switchToItem("false");
            },

            /**
             * Returns true if the panel is expanded
             * 
             * @method
             * @name RichFaces.ui.CollapsiblePanel#isExpanded
             * @return {boolean} true if the panel is expanded
             */
            isExpanded: function() {
                return this.activeItem == "true";
            },
            
            /***************************** Private Methods ********************************************************/

            __onHeaderClick : function () {
                this.switchToItem("@next");
            },

            __fireItemChange : function (oldItem, newItem) {
                return new rf.Event.fireById(this.id, "switch", {
                        id: this.id,
                        isExpanded : newItem.getName()
                    });
            },

            __fireBeforeItemChange : function (oldItem, newItem) {
                return rf.Event.fireById(this.id, "beforeswitch", {
                        id: this.id,
                        isExpanded : newItem.getName()
                    });
            }
        });
})(RichFaces.jQuery, RichFaces);
