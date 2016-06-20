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

    /**
     * Parent class for menu objects
     * 
     * @extends RichFaces.BaseComponent
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.MenuBase
     * 
     * @param componentId
     * @param options
     */
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
        this.popup = new RichFaces.ui.Popup(this.id + "_list", {
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
                var item = $(RichFaces.getDomElement(menuItemId));
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
                    var parentMenu = this.__getParentMenu();
                    if (parentMenu && this.id != parentMenu.id) {
                        parentMenu.popupElement.focus();
                        rf.ui.MenuManager.setActiveSubMenu(parentMenu);
                    }
                }
            },

            __closeChildGroups : function() {
                var i = 0;
                var menuItem;
                for (i in this.items) {
                    if (this.items.hasOwnProperty(i)) {
                        menuItem = this.items.eq(i);
                        if (this.__isGroup(menuItem)) {
                            rf.component(menuItem).hide();
                        }
                    }
                }
            },

            __getParentMenu : function() {
                var menu = $(this.element).parents('div[data-rf-parentmenu]').get(0);
                return menu ? rf.component(menu.getAttribute('data-rf-parentmenu')) : null;
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
                if (!this.__isShown() && !this.showTimeoutId) {
                    // prevent menu from hiding in case __leaveHandler was triggered
                    // (happens when showEvent is preceded by a click, e.g. "dblclick")
                    if (this.hideTimeoutId) {
                        window.clearTimeout(this.hideTimeoutId);
                        this.hideTimeoutId = null;
                    }
                    this.showTimeoutId = window.setTimeout($.proxy(function() {
                        this.show(e);
                    }, this), this.options.showDelay);
                    return false;
                }
            },

            __leaveHandler : function() {
                if (!this.hideTimeoutId) {
                    this.hideTimeoutId = window.setTimeout($.proxy(function() {
                        this.hide();
                    }, this), this.options.hideDelay);
                }
            },

            __overHandler : function() {
                window.clearTimeout(this.hideTimeoutId);
                this.hideTimeoutId = null;
            },

            destroy : function() {
                // clean up code here
                this.detach(this.id);

                rf.Event.unbind(this.popupElement, "keydown" + this.namespace);
                rf.Event.unbindById(this.id, "mouseleave");

                this.popup.destroy();
                this.popup = null;
                window.clearTimeout(this.hideTimeoutId);//clean up the hide TO

                // call parent's destroy method
                $super.destroy.call(this);
            }
        };
    })());

})(RichFaces.jQuery, RichFaces);