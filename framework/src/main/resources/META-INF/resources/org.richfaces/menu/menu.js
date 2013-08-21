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
        positionType : "DROPDOWN",
        direction : "AA",
        jointPoint : "AA",
        cssRoot : "ddm",
        cssClasses : {}
    };

    // constructor definition
    rf.ui.Menu = function(componentId, options) {
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        $.extend(this.options.cssClasses, buildCssClasses.call(this, this.options.cssRoot));
        $super.constructor.call(this, componentId, this.options);
        this.id = componentId;
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);
        this.groupList = new Array();

        this.target = this.getTarget();
        if (this.target) {
            var menu = this;
            $(document).ready(function() {
                var targetComponent = rf.component(menu.target);
                if (targetComponent && targetComponent.contextMenuAttach) {
                    targetComponent.contextMenuAttach(menu);
                } else {
                    rf.Event.bindById(menu.target, menu.options.showEvent, $.proxy(menu.__showHandler, menu), menu)
                }
            });
        }
        this.element = $(rf.getDomElement(this.id));

        if (!rf.ui.MenuManager) {
            rf.ui.MenuManager = {};
        }
        this.menuManager = rf.ui.MenuManager;
    };

    var buildCssClasses = function(cssRoot) {
        var cssClasses = {
            selectMenuCss : "rf-" +cssRoot+ "-sel",
            unselectMenuCss : "rf-" +cssRoot+ "-unsel"
        }
        return cssClasses;
    }

    rf.ui.MenuBase.extend(rf.ui.Menu);

    // define super class link
    var $super = rf.ui.Menu.$super;

    $.extend(rf.ui.Menu.prototype, rf.ui.MenuKeyNavigation);

    $.extend(rf.ui.Menu.prototype, (function() {
        return {
            name : "Menu",
            initiateGroups : function(groupOptions) {
                for (var i in groupOptions) {
                    var groupId = groupOptions[i].id;
                    if (null != groupId) {
                        this.groupList[groupId] = new rf.ui.MenuGroup(
                            groupId, {
                                rootMenuId : this.id,
                                onshow : groupOptions[i].onshow,
                                onhide : groupOptions[i].onhide,
                                horizontalOffset: groupOptions[i].horizontalOffset,
                                verticalOffset: groupOptions[i].verticalOffset,
                                jointPoint : groupOptions[i].jointPoint,
                                direction : groupOptions[i].direction,
                                cssRoot : groupOptions[i].cssRoot
                            });
                    }
                }
            },

            getTarget : function() {
                return this.id + "_label";
            },

            show : function(e) {
                if (this.menuManager.openedMenu != this.id) {
                    this.menuManager.shutdownMenu();
                    this.menuManager.addMenuId(this.id);
                    this.__showPopup();
                }
            },

            hide : function() {
                this.__hidePopup();
                this.menuManager.deletedMenuId();
            },

            select : function() {
                this.element.removeClass(this.options.cssClasses.unselectMenuCss);
                this.element.addClass(this.options.cssClasses.selectMenuCss);
            },
            unselect : function() {
                this.element.removeClass(this.options.cssClasses.selectMenuCss);
                this.element.addClass(this.options.cssClasses.unselectMenuCss);
            },

            __overHandler : function() {
                $super.__overHandler.call(this);
                this.select();
            },

            __leaveHandler : function() {
                $super.__leaveHandler.call(this);
                this.unselect();
            },

            destroy : function() {
                // clean up code here
                this.detach(this.id);

                if (this.target) {
                    rf.Event.unbindById(this.target, this.options.showEvent);
                }

                // call parent's destroy method
                $super.destroy.call(this);

            }
        };
    })());

    rf.ui.MenuManager = {
        openedMenu : null,

        activeSubMenu : null,

        addMenuId : function(menuId) {
            this.openedMenu = menuId;
        },

        deletedMenuId : function() {
            this.openedMenu = null;
        },

        shutdownMenu : function() {
            if (this.openedMenu != null) {
                rf.component(rf.getDomElement(this.openedMenu)).hide();
            }
            this.deletedMenuId();
        },

        setActiveSubMenu : function(submenu) {
            this.activeSubMenu = submenu;
        },

        getActiveSubMenu : function() {
            return this.activeSubMenu;
        }
    }
})(RichFaces.jQuery, RichFaces);