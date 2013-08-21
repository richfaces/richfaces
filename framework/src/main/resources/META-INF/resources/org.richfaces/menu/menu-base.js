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

    var defaultOptions = {
        mode : 'server',
        attachToBody : false,
        showDelay : 50,
        hideDelay : 300,
        verticalOffset : 0,
        horizontalOffset : 0,
        showEvent : 'mouseover',
        positionOffset : [0, 0],
        cssRoot : "ddm",
        cssClasses : {}
    };

    rf.ui.MenuBase = function(componentId, options) {
        $super.constructor.call(this, componentId, options);
        this.id = componentId;
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);

        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        $.extend(this.options.cssClasses, buildCssClasses.call(this, this.options.cssRoot));

        this.attachToDom(componentId);

        this.element = rf.getDomElement(this.id);

        this.displayed = false;

        this.options.positionOffset = [this.options.horizontalOffset, this.options.verticalOffset];
        this.popup = new rf.ui.Popup(this.id + "_list", {
                attachTo : this.id,
                direction : this.options.direction,
                jointPoint : this.options.jointPoint,
                positionType : this.options.positionType,
                positionOffset : this.options.positionOffset,
                attachToBody : this.options.attachToBody
            });

        this.selectedGroup = null;

        rf.Event.bindById(this.id, "mouseenter", $.proxy(this.__overHandler, this), this);
        rf.Event.bindById(this.id, "mouseleave", $.proxy(this.__leaveHandler, this), this);

        this.popupElement = rf.getDomElement(this.popup.id);
        this.popupElement.tabIndex = -1;

        this.__updateItemsList();

        rf.Event.bind(this.items, "mouseenter", $.proxy(this.__itemMouseEnterHandler, this), this);

        this.currentSelectedItemIndex = -1;
        var navEventHandlers;
        navEventHandlers = {};
        navEventHandlers["keydown" + this.namespace] = this.__keydownHandler;

        rf.Event.bind(this.popupElement, navEventHandlers, this);
    };

    var buildCssClasses = function(cssRoot) {
        var cssClasses = {
            itemCss : "rf-" +cssRoot+ "-itm",
            selectItemCss : "rf-" +cssRoot+ "-itm-sel",
            unselectItemCss : "rf-" +cssRoot+ "-itm-unsel",
            disabledItemCss : "rf-" +cssRoot+ "-itm-dis",
            labelCss: "rf-" +cssRoot+ "-lbl",
            listCss : "rf-" +cssRoot+ "-lst",
            listContainerCss : "rf-" +cssRoot+ "-lst-bg"
        }
        return cssClasses;
    }

    rf.BaseComponent.extend(rf.ui.MenuBase);

    // define super class link
    var $super = rf.ui.MenuBase.$super;

    $.extend(rf.ui.MenuBase.prototype, (function() {
        return {
            name : "MenuBase",

            show : function() {
                this.__showPopup();
            },

            hide : function() {
                this.__hidePopup();
            },

            processItem : function(item) {
                if (item && item.attr('id') && !this.__isDisabled(item) && !this.__isGroup(item)) {
                    this.invokeEvent("itemclick", rf.getDomElement(this.id), null);
                    this.hide();
                }
            },

            activateItem : function(menuItemId) {
                var item = $(rf.getDomElement(menuItemId));
                rf.Event.fireById(item.attr('id'), 'click');
            },

            __showPopup : function(e) {
                if (!this.__isShown()) {
                    this.invokeEvent("show", rf.getDomElement(this.id), null);
                    this.popup.show(e);
                    this.displayed = true;
                    rf.ui.MenuManager.setActiveSubMenu(rf.component(this.element));
                }
                this.popupElement.focus();
            },

            __hidePopup : function() {
                window.clearTimeout(this.showTimeoutId);
                this.showTimeoutId = null;
                if (this.__isShown()) {
                    this.invokeEvent("hide", rf.getDomElement(this.id), null);
                    this.__closeChildGroups();
                    this.popup.hide();
                    this.displayed = false;
                    this.__deselectCurrentItem();
                    this.currentSelectedItemIndex = -1;
                    var parentMenu = rf.component(this.__getParentMenu());
                    if (this.id != parentMenu.id) {
                        parentMenu.popupElement.focus();
                        rf.ui.MenuManager.setActiveSubMenu(parentMenu);
                    }
                }
            },

            __closeChildGroups : function() {
                var i = 0;
                var menuItem;
                for (i in this.items) {
                    menuItem = this.items.eq(i);
                    if (this.__isGroup(menuItem)) {
                        rf.component(menuItem).hide();
                    }
                }
            },

            __getParentMenuFromItem : function(item) {
                var menu;
                if (item)
                    menu = item.parents('div.' + this.options.cssClasses.itemCss).has('div.' + this.options.cssClasses.listContainerCss).eq(1);
                if (menu && menu.length > 0)
                    return menu;
                else {
                    menu = item.parents('div.' + this.options.cssClasses.labelCss);
                    if (menu && menu.length > 0) {
                        return menu;
                    }
                    else {
                        return null;
                    }
                }
            },

            __getParentMenu : function() {
                var menu = $(this.element).parents('div.' + this.options.cssClasses.itemCss).has('div.' + this.options.cssClasses.listContainerCss).eq(0);
                if (menu && menu.length > 0) {
                    return menu;
                }
                else {
                    var item = this.items.eq(0);
                    return this.__getParentMenuFromItem(item);
                }
            },

            __isGroup : function(item) {
                return item.find('div.' + this.options.cssClasses.listCss).length > 0;
            },

            __isDisabled : function(item) {
                return item.hasClass(this.options.cssClasses.disabledItemCss);
            },

            __isShown : function() {
                return this.displayed;

            },

            __itemMouseEnterHandler : function(e) {
                var item = this.__getItemFromEvent(e);
                if (item) {
                    //this.__selectItem(item);
                    if (this.currentSelectedItemIndex != this.items.index(item)) {
                        this.__deselectCurrentItem();
                        this.currentSelectedItemIndex = this.items.index(item);
                    }
                }
            },

            __selectItem : function(item) {
                if (!rf.component(item).isSelected) {
                    rf.component(item).select();
                }
            },

            __getItemFromEvent : function(e) {
                return $(e.target).closest("." + this.options.cssClasses.itemCss,e.currentTarget).eq(0);
            },

            __showHandler : function(e) {
                if (!this.__isShown()) {
                    this.showTimeoutId = window.setTimeout($.proxy(function() {
                        this.show(e);
                    }, this), this.options.showDelay);
                    return false;
                }
            },

            __leaveHandler : function() {
                this.hideTimeoutId = window.setTimeout($.proxy(function() {
                    this.hide();
                }, this), this.options.hideDelay);
            },

            __overHandler : function() {
                window.clearTimeout(this.hideTimeoutId);
                this.hideTimeoutId = null;
            },

            destroy : function() {
                // clean up code here
                this.detach(this.id);

                rf.Event.unbind(this.popupElement, "keydown" + this.namespace);

                this.popup.destroy();
                this.popup = null;

                // call parent's destroy method
                $super.destroy.call(this);
            }
        };
    })());

})(RichFaces.jQuery, RichFaces);