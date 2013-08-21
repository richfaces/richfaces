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
        showEvent : 'mouseenter',
        direction : "AA",
        jointPoint : "AA",
        positionType : "DDMENUGROUP",
        showDelay : 300
    }
    // constructor definition
    rf.ui.MenuGroup = function(componentId, options) {
        this.id = componentId;
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        $super.constructor.call(this, componentId, this.options);
        this.namespace = this.namespace || "."
            + rf.Event.createNamespace(this.name, this.id);
        this.attachToDom(componentId);

        rf.Event.bindById(this.id, this.options.showEvent, $.proxy(
            this.__showHandler, this), this);

        this.rootMenu = rf.component(this.options.rootMenuId);

        this.shown = false;
        this.jqueryElement = $(this.element);

    };

    rf.ui.MenuBase.extend(rf.ui.MenuGroup);

    // define super class link
    var $super = rf.ui.MenuGroup.$super;

    $.extend(rf.ui.MenuGroup.prototype, rf.ui.MenuKeyNavigation);

    $.extend(rf.ui.MenuGroup.prototype, (function() {
        return {
            name : "MenuGroup",
            show : function() {
                var id = this.id;
                if (this.rootMenu.groupList[id] && !this.shown) {
                    this.rootMenu.invokeEvent("groupshow", rf
                        .getDomElement(this.rootMenu.id),
                        null);
                    this.__showPopup();
                    this.shown = true;
                }
            },
            hide : function() {
                var menu = this.rootMenu;
                if (menu.groupList[this.id] && this.shown) {
                    menu.invokeEvent("grouphide", rf
                        .getDomElement(menu.id), null);
                    this.__hidePopup();
                    this.shown = false;
                }
            },

            select : function() {
                this.jqueryElement.removeClass(this.options.cssClasses.unselectItemCss);
                this.jqueryElement.addClass(this.options.cssClasses.selectItemCss);
            },
            unselect : function() {
                this.jqueryElement.removeClass(this.options.cssClasses.selectItemCss);
                this.jqueryElement.addClass(this.options.cssClasses.unselectItemCss);
            },

            __showHandler : function() {
                this.select();
                $super.__showHandler.call(this);
            },
            __leaveHandler : function() {
                window.clearTimeout(this.showTimeoutId);
                this.showTimeoutId = null;
                this.hideTimeoutId = window.setTimeout($.proxy(
                    function() {
                        this.hide();
                    }, this), this.options.hideDelay);
                this.unselect();
            },

            destroy : function() {
                // clean up code here
                this.detach(this.id);
                // call parent's destroy method
                $super.destroy.call(this);
            }
        }

    })());
})(RichFaces.jQuery, RichFaces);