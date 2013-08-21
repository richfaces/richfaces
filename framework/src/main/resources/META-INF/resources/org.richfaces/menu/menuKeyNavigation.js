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

(function($, rf) {

    rf.ui = rf.ui || {};
    //rf.ui.MenuKeyNavigation = rf.ui.MenuKeyNavigation || {};

    rf.ui.MenuKeyNavigation = {

        __updateItemsList : function() {
            var items = $('.' + this.options.cssClasses.listContainerCss + ':first',
                this.popup.popup).find('>.' + this.options.cssClasses.itemCss).not(
                '.' + this.options.cssClasses.disabledItemCss);
            return (this.items = items);
        },

        __selectPrev : function() {
            if (-1 == this.currentSelectedItemIndex) {
                this.currentSelectedItemIndex = this.items.length - 1;
            } else {
                this.__deselectCurrentItem();
            }

            if (this.currentSelectedItemIndex > 0) {
                this.currentSelectedItemIndex--;
            } else {
                this.currentSelectedItemIndex = this.items.length - 1;
            }

            this.__selectCurrentItem();
        },

        __selectNext : function() {
            if (-1 != this.currentSelectedItemIndex) {
                this.__deselectCurrentItem();
            }
            if (this.currentSelectedItemIndex < this.items.length - 1) {
                this.currentSelectedItemIndex++;
            } else {
                this.currentSelectedItemIndex = 0;
            }

            this.__selectCurrentItem();
        },

        __deselectCurrentItem : function() {
            this.__deselectByIndex(this.currentSelectedItemIndex);
        },

        __selectCurrentItem : function() {
            this.__selectByIndex(this.currentSelectedItemIndex);
        },

        __selectFirstItem : function() {
            this.currentSelectedItemIndex = 0;
            this.__selectCurrentItem();
        },

        __selectByIndex : function(index) {
            if (-1 != index) {
                rf.component(this.items.eq(index)).select();
            }
        },

        __deselectByIndex : function(index) {
            if (index > -1) {
                rf.component(this.items.eq(index)).unselect();
            }

        },

        __openGroup : function() {
            var item = this.__getItemByIndex(this.currentSelectedItemIndex);
            if (this.__isGroup(item)) {
                rf.component(item).show();
                rf.component(item).__selectFirstItem();
                this.active = false;
            }
        },

        __closeGroup : function() {
            var item = this.__getItemByIndex(this.currentSelectedItemIndex);
            if (this.__isGroup(item)) {
                rf.component(item).__deselectCurrentItem();
                rf.component(item).hide();
                this.active = true;
            }
        },

        __returnToParentMenu : function() {
            var item = this.__getItemByIndex(this.currentSelectedItemIndex);
            var menu;
            menu = this.__getParentMenu() || this.__getParentMenuFromItem(item);
            if (menu != null && this.id != rf.component(menu).id) {
                this.hide();
                rf.component(menu).popupElement.focus();
            } else {
                this.hide();
            }
        },

        __activateMenuItem : function() {
            var item = this.__getCurrentItem();
            if (item) {
                menuItemId = item.attr('id');
                this.activateItem(menuItemId);
            }
        },

        __getItemByIndex : function(index) {
            if (index > -1) {
                return this.items.eq(index);
            } else {
                return null;
            }
        },

        __getCurrentItem : function() {
            return this.__getItemByIndex(this.currentSelectedItemIndex);
        },

        __keydownHandler : function(e) {
            var code;

            if (e.keyCode) {
                code = e.keyCode;
            } else if (e.which) {
                code = e.which;
            }

            activeMenu = rf.ui.MenuManager.getActiveSubMenu();

            if (this.popup.isVisible()) {
                switch (code) {
                    case rf.KEYS.DOWN:
                        e.preventDefault();
                        activeMenu.__selectNext();
                        //this.__setInputFocus();
                        break;

                    case rf.KEYS.UP:
                        e.preventDefault();
                        activeMenu.__selectPrev();
                        //this.__setInputFocus();
                        break;
                    case rf.KEYS.LEFT:
                        e.preventDefault();
                        activeMenu.__returnToParentMenu();
                        break;

                    case rf.KEYS.RIGHT:
                        e.preventDefault();
                        activeMenu.__openGroup();
                        break;

                    case rf.KEYS.ESC:
                        e.preventDefault();
                        activeMenu.__returnToParentMenu();
                        break;

                    case rf.KEYS.RETURN:
                        e.preventDefault();
                        activeMenu.__activateMenuItem();
                        //this.__setInputFocus();
                        //return false;
                        break;
                }
                e.stopPropagation();
            }
        }
    }
})(jQuery, RichFaces);