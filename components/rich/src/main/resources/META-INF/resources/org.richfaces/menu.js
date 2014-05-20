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
                    $('body').on('rich:ready' + menu.namespace, '[id="' + menu.target + '"]', function() {targetComponent.contextMenuAttach(menu)});
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
                    var targetComponent = rf.component(this.target);
                    if (targetComponent && targetComponent.contextMenuAttach) {
                        $('body').off('rich:ready' + this.namespace, '[id="' + this.target + '"]');
                    }
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