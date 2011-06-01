(function($, rf) {
    rf.ui = rf.ui || {};

    var defaultOptions = {
        positionType : "DROPDOWN",
        direction : "AA",
        jointPoint : "AA",
        selectMenuCss : "rf-ddm-sel",
        unselectMenuCss : "rf-ddm-unsel"
    };

    // constructor definition
    rf.ui.Menu = function(componentId, options) {
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        $super.constructor.call(this, componentId, this.options);
        this.id = componentId;
        this.namespace = this.namespace || "."
            + rf.Event.createNamespace(this.name, this.id);
        this.groupList = new Array();

        rf.Event.bindById(this.id + "_label", this.options.showEvent, $.proxy(
            this.__showHandler, this), this);
        this.element = $(rf.getDomElement(this.id));

        if (!rf.ui.MenuManager)
            rf.ui.MenuManager = {};
        this.menuManager = rf.ui.MenuManager;
    };

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
                        this.groupList[groupId] = new RichFaces.ui.MenuGroup(
                            groupId, {
                                rootMenuId : this.id,
                                onshow : groupOptions[i].onshow,
                                onhide : groupOptions[i].onhide,
                                horizontalOffset: groupOptions[i].horizontalOffset,
                                verticalOffset: groupOptions[i].verticalOffset,
                                jointPoint : groupOptions[i].jointPoint,
                                direction : groupOptions[i].direction
                            });
                    }
                }
            },

            show : function(e) {
                if (this.menuManager.openedMenu != this.id) {
                    this.menuManager.shutdownMenu();
                    this.menuManager.addMenuId(this.id);
                    this.__showPopup(e);
                }
            },

            hide : function() {
                this.__hidePopup();
                this.menuManager.deletedMenuId();
            },

            select : function() {
                this.element.removeClass(this.options.unselectMenuCss);
                this.element.addClass(this.options.selectMenuCss);
            },
            unselect : function() {
                this.element.removeClass(this.options.selectMenuCss);
                this.element.addClass(this.options.unselectMenuCss);
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

                rf.Event.unbindById(this.id + "_label", this.options.showEvent);

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
                rf.$(rf.getDomElement(this.openedMenu)).hide();
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
})(jQuery, RichFaces);