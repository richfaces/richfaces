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

    rf.ui.TogglePanelItem = rf.BaseComponent.extendClass({

        // class name
        name:"TogglePanelItem",

        init : function (componentId, options) {
            // call constructor of parent class
            rf.BaseComponent.call(this, componentId);
            this.attachToDom(componentId);

            this.options = options;
            this.name = this.options.name;
            this.togglePanelId = this.options.togglePanelId;
            this.switchMode = this.options.switchMode;
        },

        /***************************** Public Methods *****************************************************************/
        /**
         * @methodOf TogglePanelItem
         * @name TogglePanelItem#getName
         *
         * @return {String} panel item name
         */
        getName: function () {
            return this.options.name;
        },

        /**
         * @methodOf
         * @name TogglePanelItem#getTogglePanel
         *
         * @return {TogglePanel} parent TogglePanel
         * */
        getTogglePanel : function () {
            return rf.$(this.togglePanelId);
        },

        /**
         * @methodOf
         * @name TogglePanelItem#isSelected
         *
         * @return {Boolean} true if this panel item is selected in the parent toggle panel
         * */
        isSelected : function () {
            return this.getName() == this.getTogglePanel().getSelectItem();
        },


        /***************************** Private Methods ****************************************************************/

        /**
         * @private
         *
         * used in TogglePanel
         * */
        __enter : function () {
            rf.getDomElement(this.id).style.display = "block";

            return this.__fireEnter();
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

            rf.getDomElement(this.id).style.display = "none";
            return true;
        },

        __fireLeave : function () {
            return rf.Event.fireById(this.id, "__leave");
        },

        __fireEnter : function () {
            return rf.Event.fireById(this.id, "__enter");
        },

        // class stuff
        destroy: function () {
            //                 rf.Event.unbindById(this.options.buttonId, "."+this.namespace);
            //                 rf.Event.unbindById(this.componentId, "."+this.namespace);
            //                $super.destroy.call(this);
        }
    });
})(jQuery, RichFaces);
